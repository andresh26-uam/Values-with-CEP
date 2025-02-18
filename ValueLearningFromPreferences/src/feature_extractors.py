import numpy as np
import torch
from stable_baselines3.common.preprocessing import get_flattened_obs_dim
from stable_baselines3.common.torch_layers import BaseFeaturesExtractor

def one_hot_encoding(a, n, dtype=np.float32):
    v = np.zeros(shape=(n,), dtype=dtype)
    v[a] = 1.0
    return v


class OneHotFeatureExtractor(BaseFeaturesExtractor):
    """
    A custom feature extractor that performs one-hot encoding on integer observations.
    """

    def __init__(self, observation_space, n_categories):
        # The output of the extractor is the size of one-hot encoded vectors
        super(OneHotFeatureExtractor, self).__init__(
            observation_space, features_dim=n_categories)
        self.n_categories = n_categories

    def forward(self, observations):
        # Convert observations to integers (if needed) and perform one-hot encoding
        """batch_size = observations.shape[0]
        one_hot = torch.zeros((batch_size, self.n_categories), device=observations.device)

        one_hot.scatter_(1, observations.long(), 1)"""
        with torch.no_grad():
            if len(observations.shape) > 2:
                observations = torch.squeeze(observations, dim=1)
            if observations.shape[-1] != int(self.features_dim):

                ret = torch.functional.F.one_hot(
                    observations.long(), num_classes=int(self.features_dim)).float()
            else:
                ret = observations
            return ret


class ObservationMatrixFeatureExtractor(BaseFeaturesExtractor):
    """
    A custom feature extractor that performs one-hot encoding on integer observations.
    """

    def __init__(self, observation_space, observation_matrix):
        # The output of the extractor is the size of one-hot encoded vectors
        super(ObservationMatrixFeatureExtractor, self).__init__(
            observation_space, features_dim=observation_matrix.shape[1])
        self.observation_matrix = torch.tensor(
            observation_matrix, dtype=torch.float32, requires_grad=False)

    def forward(self, observations):
        # Convert observations to integers (if needed) and perform one-hot encoding
        """batch_size = observations.shape[0]
        one_hot = torch.zeros((batch_size, self.n_categories), device=observations.device)

        one_hot.scatter_(1, observations.long(), 1)"""
        with torch.no_grad():
            idx = observations
            """if idx.shape[-1] > 1:
                ret =  torch.vstack([self.observation_matrix[id] for id in idx.bool()])
            else:
                ret =  torch.vstack([self.observation_matrix[id] for id in idx.long()])"""
            if idx.shape[-1] > 1:
                # Convert idx to a boolean mask and use it to index the observation_matrix
                mask = idx.bool()
                # Get indices where mask is True
                selected_indices = mask.nonzero(as_tuple=True)[-1]
                assert len(selected_indices) == observations.shape[0]
                # Select the first 32 True indices to maintain the output shape
                ret = self.observation_matrix[selected_indices]
            else:
                # Directly index the observation_matrix using long indices
                ret = self.observation_matrix[idx.view(-1).long()]
        return ret


class FeatureExtractorFromVAEnv(BaseFeaturesExtractor):
    def __init__(self, observation_space,  **kwargs) -> None:
        super().__init__(observation_space, get_flattened_obs_dim(observation_space))

        self.env = kwargs['env']
        self.dtype = kwargs['dtype']
        self.torch_obs_mat = torch.tensor(
            self.env.observation_matrix, dtype=torch.float32)

    def forward(self, observations: torch.Tensor) -> torch.Tensor:
        return self.torch_obs_mat[observations.long()]