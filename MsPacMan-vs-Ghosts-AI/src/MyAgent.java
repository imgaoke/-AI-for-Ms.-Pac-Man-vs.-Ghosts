import java.util.*;
import search.Problem;

import controllers.pacman.PacManControllerBase;
import game.core.Game;
import game.core.GameView;
import java.awt.Color;



public final class MyAgent extends PacManControllerBase
{	
	public class MsPacmanMaze implements Problem<Integer, Integer> {

		public Integer initialState() { 
			return game.getCurPacManLoc(); }

		public List<Integer> actions(Integer state) { 
			List<Integer> list = new ArrayList<Integer>();

			for (int i = 0; i <= 3; i++){
				if (game.getNeighbour(state, i) != -1){
					list.add(i);
				}
			}
			return list; 
		}

		public Integer result(Integer state, Integer action) { 
			return game.getNeighbour(state, action);
		}
		
		public boolean isGoal(Integer state) { 
			var result = (game.getPillIndex(state) != -1 && game.checkPill(game.getPillIndex(state))) ||
						 (game.getPowerPillIndex(state) != -1 && game.checkPowerPill(game.getPowerPillIndex(state))) ||
						 game.getFruitLoc() == state;// ||
			return result;
		}

		public double cost(Integer state, Integer action) {
			int nextState = game.getNeighbour(state, action);
			double score = 0;
			for (int i = 0; i <= 3; i++){
				int ghostLoc = game.getCurGhostLoc(i);
				if (ghostLoc == nextState && !game.isEdible(i)){
					score = 30.0;
					break;
				}
				if (game.getPathDistance(ghostLoc, nextState) <= 8 && !game.isEdible(i)){
					score = 10.0;
				}
			}

			if (nextState == game.getFruitLoc() || (game.getPowerPillIndex(state) != -1 && game.checkPowerPill(game.getPowerPillIndex(state)))){
				score += 0.01;
			}
			else{
				score += 0.2;
			}

			return score;
		}
	}



	@Override
	public void tick(Game game, long timeDue) {
		var maze = new MsPacmanMaze();
		var solution = Ucs.search(maze);
		// Code your agent here.
		int current=game.getCurPacManLoc();
		int action = solution.actions.get(0);
		int[] path = new int[solution.actions.size()];
		for (int i = 0; i < solution.actions.size(); i++){
			var nextState = game.getNeighbour(current, solution.actions.get(i));
			path[i] = nextState;
			current = nextState;
		}
		GameView.addPoints(game, Color.GREEN, path);
		pacman.set(action);
	}
}
