# %%

from functools import partial
import time

from seals import base_envs
import numpy as np

from stable_baselines3.common.vec_env import DummyVecEnv

from src.mce_irl_for_road_network import (
    MCEIRL_RoadNetwork,
)
from imitation.data import rollout
import torch

from src.policies import SimplePolicy, ValueIterationPolicy, check_policy_gives_optimal_paths
from src.network_env import DATA_FOLDER, FeaturePreprocess, FeatureSelection, RoadWorldPOMDPStateAsTuple
from src.reward_functions import TrainingModes, ProfiledRewardFunction
from src.src_rl.aggregations import SumScore
from src.values_and_costs import BASIC_PROFILES
from src.utils.load_data import ini_od_dist
from utils import split_od_train_test

from torch import nn


# CUSTOM

log_interval = 10  # interval between training status logs
seed = 260 # random seed for parameter initialization
rng = np.random.default_rng(260)

cv = 0  # cross validation process [0, 1, 2, 3, 4] # TODO (?)
size = 100  # size of training data [100, 1000, 10000]

device = torch.device('cuda') if torch.cuda.is_available() else torch.device('cpu')

"""seeding"""

np.random.seed(seed)
torch.manual_seed(seed)
rng = np.random.default_rng(seed)

"""environment"""
edge_p = f"{DATA_FOLDER}/edge.txt"
network_p = f"{DATA_FOLDER}/transit.npy"
path_feature_p = f"{DATA_FOLDER}/feature_od.npy"
train_p = f"{DATA_FOLDER}/cross_validation/train_CV%d_size%d.csv" % (cv, size)
test_p = f"{DATA_FOLDER}/cross_validation/test_CV%d.csv" % cv
node_p = f"{DATA_FOLDER}/node.txt"


# %%
"""inialize road environment"""

PROFILE = (1,0,0)
HORIZON = 100
DEST = 413
DEST2 = 107
USE_OPTIMAL_REWARD_AS_FEATURE = False
SINGLE_DEST = False
EXAMPLE_PROFILES: list = BASIC_PROFILES

PREPROCESSING = FeaturePreprocess.NORMALIZATION
USE_OM = False
STOCHASTIC = False
USE_DIJSTRA = False if USE_OM else False if STOCHASTIC else True # change True only when USE_OM is not True.

N_EXPERT_SAMPLES_PER_OD = 1 if USE_OM is True else 10 if STOCHASTIC else 3# change True only when USE_OM is not True.
FEATURE_SELECTION = FeatureSelection.ONLY_COSTS


SAVED_REWARD_NET_FILE = "profiled_reward_function_trained.pt"

if __name__ == "__main__":
    od_list, od_dist = ini_od_dist(train_p)
    print("DEBUG MODE", __debug__)
    
    od_list_train, od_list_test, _, _= split_od_train_test(od_list, od_dist, split=0.8, to_od_list_int=True)

    env_creator = partial(RoadWorldPOMDPStateAsTuple, network_path=network_p, edge_path=edge_p, node_path=node_p, path_feature_path = path_feature_p, 
                        pre_reset=(od_list, od_dist), profile=PROFILE, visualize_example=False, horizon=HORIZON,
                        feature_selection=FEATURE_SELECTION,
                        feature_preprocessing=PREPROCESSING, 
                        use_optimal_reward_per_profile=USE_OPTIMAL_REWARD_AS_FEATURE)
    env_single = env_creator()


    if SINGLE_DEST:
        od_list = [str(state) + '_' + str(DEST) for state in env_single.valid_edges]
        env_creator = partial(RoadWorldPOMDPStateAsTuple, network_path=network_p, edge_path=edge_p, node_path=node_p, path_feature_path = path_feature_p, pre_reset=(od_list, od_dist), profile=PROFILE, visualize_example=False, horizon=HORIZON,
                            one_hot_features=FEATURE_SELECTION,
                            normalization_or_standarization_or_none=PREPROCESSING, 
                            use_optimal_reward_per_profile=USE_OPTIMAL_REWARD_AS_FEATURE)
        env_single = env_creator()

    pre_created_env = base_envs.ExposePOMDPStateWrapper(env_single)

    state_env_creator = lambda: pre_created_env

    state_venv = DummyVecEnv([state_env_creator] * 1)

    reward_net = ProfiledRewardFunction(
        environment=env_single,
        use_state=False,
        use_action=False,
        use_next_state=True,
        use_done=False,
        hid_sizes=[3,],
        reward_bias=-0.0,
        #partial(nn.Softplus, beta=1, threshold=5)
        activations=[nn.Identity, nn.Identity]
    )
    reward_net.set_profile(PROFILE)

    print(reward_net.values_net)

    start_vi = time.time()
    pi_with_d_per_profile = {
        pr: ValueIterationPolicy(env_single,score_calculator=SumScore()).value_iteration(0.000001, verbose=True, custom_reward=lambda s,a,d: env_single.get_reward(s,a,d,tuple(pr))) for pr in EXAMPLE_PROFILES
    }
    end_vi = time.time()

    print("VI TIME: ", end_vi - start_vi)

    if False:
        start_mce = time.time()
        _, _, pi = mce_partition_fh(env_single, reward=env_single.reward_matrix, verbose=True)

        print("MCE TIME: ", time.time() - start_mce)

        equivalent_pi = equivalent_pi_with_d[413,:,:]

        print(list(equivalent_pi[0:5] - pi[0:5]))
        dif = equivalent_pi - pi 
        w = np.where(dif != 0)
        print(w)
        print(dif[w].tolist())
        # TODO WTF por que mce partition tarda muchisimo mas que value iteration?????
        assert np.allclose(equivalent_pi - pi, 0.0)

    print("mce partition done")


    #expert_policyAlgo_eco: PolicyAlgo = PolicyAlgo.from_policy_matrix(equivalent_pi_with_d, real_env = env_single)

    #expert_demonstrations_eco = expert_policyAlgo_eco.sample_trajectories(stochastic=False, repeat_per_od=1, profile=PROFILE)


    expert_policyAlgo: SimplePolicy = SimplePolicy.from_policy_matrix(pi_with_d_per_profile, real_env = env_single)

    expert_demonstrations_all_profiles = expert_policyAlgo.sample_trajectories(stochastic=False, repeat_per_od=N_EXPERT_SAMPLES_PER_OD, with_profiles=EXAMPLE_PROFILES)

    if __debug__:
        check_policy_gives_optimal_paths(env_single, expert_policyAlgo, profiles=EXAMPLE_PROFILES)

    #assert len(expert_demonstrations_all_profiles) == 3*len(expert_demonstrations_eco)
    print("PATHS CHECKED WITH VI")

    #_, precise_stochastic_expert_om = mce_occupancy_measures(env_single, pi=pi, expert_demonstrations=expert_demonstrations)
    #print("OC done")
    #print(precise_stochastic_expert_om)

    #print(om[(107,DEST)]-demo_om[(107, DEST)])
    #assert np.allclose(np.abs(om[(107,DEST)]-demo_om[(107, DEST)]), 1e-2)

    #print(env_single.reward_matrix)
    #exit(0) # TODO: 
    # OPCION 1: Probar mce_occupancy measures con experto estocástico de VI. USar mce_occupancy para las policies internas.
    # OPCION 1.2: Probar mce_occupancy measures con experto estocástico sampleado de get_demo_oms_from_trajectories
    # OPCION 2: Todo con get_demo_oms_from_trajectories setting policy each step y sample_trajectories(PolicyAlgo.from_sb3_policy(mce_irl.policy, real_env = env_single))
    # 2.a. Todo estocastico (stochastic True, repeat_per_od > 1)
    # 2.b: todo deterministico (stochastic False, repeat_per_od = 1)

    path, edge_path = expert_policyAlgo.sample_path(start=107, des = DEST, stochastic=False, profile=EXAMPLE_PROFILES[0],t_max=1000)
    print("EXPERT PATH 107 DEST", edge_path)

    env_single.render(caminos_by_value={'eco': [path,]}, file="test_mce_expert_partition.png", show=False,show_edge_weights=False)


    # ESTO ES VALUE ITERATION PURO CON LA IMPLEMENTACION MIA ANTIGUA NO DEDICADA A ESTO.
    """EXPERT_FROM_FILE = False
    vip = ValueIterationPolicy(env_single, score_calculator=SumScore())
    vip.train(alpha=0.0001, t_max=200, profile=PROFILE)
    path, edge_path = vip.sample_path(start=107, des = DEST, stochastic=False, profile=PROFILE,t_max=HORIZON)
    print(edge_path)
    env_single.render(caminos_by_value={'eco': [path,]}, file="test_mce_expert_vi.png", show=False,show_edge_weights=True)
    """
    # ROLLOUTS NO. Sample_trajectories y fuera.
    #rollouts_custom = create_expert_trajectories(env_creator, from_df_expert_file_or_policy='expert_session_2.csv' if EXPERT_FROM_FILE else vip, profile=PROFILE, repeat_samples=1, only_edges=True)


    # FUNCION LINEAL
    """reward_net = reward_nets.BasicRewardNet(
        env_single.observation_space,
        env_single.action_space,
        hid_sizes=[100,16,3],
        activation=nn.Tanh,
        use_action=False,
        use_done=False,
        use_next_state=False,
    )"""




    st = time.time()
    mce_irl = MCEIRL_RoadNetwork(
        expert_policy=expert_policyAlgo,
        expert_trajectories=expert_demonstrations_all_profiles, # los rollout no me fio en absoluto.
        env=env_single,
        reward_net=reward_net,
        log_interval=5,
        optimizer_kwargs={"lr": 0.3, "weight_decay": 0.00001},
        mean_vc_diff_eps=0.000001,
        rng=rng,
        overlaping_percentage=0.99,
        use_expert_policy_oms_instead_of_monte_carlo=USE_OM,
        n_repeat_per_od_monte_carlo = N_EXPERT_SAMPLES_PER_OD,
        #sampling_profiles=EXAMPLE_PROFILES,
        grad_l2_eps=0.0000001,
        fd_lambda=0.0,
        use_dijkstra=USE_DIJSTRA,
        stochastic_expert=STOCHASTIC,
        od_list_train=od_list_train,
        od_list_test=od_list_test,
        training_mode=TrainingModes.VALUE_LEARNING
    )
    print(list(mce_irl.reward_net.parameters()))
    print("TRAINING STARTED")

    predicted_rewards_per_profile, train_stats, test_stats = mce_irl.train(max_iter=1000,render_partial_plots=False)


    end_time = time.time()
    print("TIME: ", end_time - st)
    print("FINAL PARAMETERS: ")
    print(list(mce_irl.reward_net.parameters()))

    reward_net.save_checkpoint(SAVED_REWARD_NET_FILE)

    env_single.render(caminos_by_value={}, file='me_learned_costs_w_label.png', show=False, show_edge_weights=True, custom_weights={pr: -predicted_rewards_per_profile[pr] for pr in predicted_rewards_per_profile.keys()}, custom_weights_dest=413)
    env_single.render(caminos_by_value={}, file='me_learned_costs.png', show=False, show_edge_weights=False, custom_weights={pr: -predicted_rewards_per_profile[pr] for pr in predicted_rewards_per_profile.keys()}, custom_weights_dest=413)
    env_single.render(caminos_by_value={}, file='me_real_costs.png', show=False, show_edge_weights=False)


    mce_irl.policy.set_profile(PROFILE)
    sampler: SimplePolicy = SimplePolicy.from_sb3_policy(mce_irl.policy, real_env = env_single)
    path, edge_path = sampler.sample_path(start=107, des = 413, stochastic=False, profile=PROFILE,t_max=HORIZON)

    env_single.render(caminos_by_value={'eco': [path,]}, file="me_learned_paths.png", show=False,show_edge_weights=True)


    #print(mce_irl.policy)
    #print("PI", mce_irl.policy.pi)

    mce_irl.policy.set_profile(PROFILE)
    imitation_trajs = rollout.generate_trajectories(
        policy=mce_irl.policy,
        venv=state_venv,
        sample_until=rollout.make_min_episodes(100),
        rng=rng,
        deterministic_policy=True
    )
    print("Imitation stats: ", rollout.rollout_stats(imitation_trajs))

    
    #reward_net = ProfiledRewardFunction.from_checkpoint(SAVED_REWARD_NET_FILE)
