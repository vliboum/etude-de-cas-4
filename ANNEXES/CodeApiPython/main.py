from fastapi import FastAPI
from pydantic import BaseModel
from typing import List
import numpy as np
from sklearn.neighbors import NearestNeighbors

app = FastAPI(title="GamesUP - Recommender System API")

class SimpleRecommendationRequest(BaseModel):
    target_price: float
    all_ids: List[int]
    all_prices: List[float]
    k: int = 3

@app.get("/")
def read_root():
    return {"status": "Moteur de recommandation en ligne"}

@app.post("/recommend")
def get_recommendations(request: SimpleRecommendationRequest):
    if not request.all_prices or len(request.all_prices) < 2:
        return {"recommended_game_ids": []}

    # 1. Construction des matrices Numpy à partir des listes simples
    X = np.array(request.all_prices).reshape(-1, 1)
    target = np.array([[request.target_price]])

    # 2. Sécurité du nombre de voisins
    n_neighbors = min(request.k, len(X))

    # 3. Calcul KNN
    knn = NearestNeighbors(n_neighbors=n_neighbors, metric="euclidean")
    knn.fit(X)
    distances, indices = knn.kneighbors(target)

    # 4. Association des indices trouvés aux vrais IDs de jeux
    recommended_ids = [int(request.all_ids[idx]) for idx in indices[0]]

    return {
        "recommended_game_ids": recommended_ids
    }