package ufl.cs1.controllers;

import game.controllers.DefenderController;
import game.models.Attacker;
import game.models.Defender;
import game.models.Game;
import game.models.Node;

import java.util.List;

public final class StudentController implements DefenderController
{
	public void init(Game game) { }

	public void shutdown(Game game) { }

	public int[] update(Game game,long timeDue)
	{
		int[] actions = new int[Game.NUM_DEFENDER];
		List<Defender> enemies = game.getDefenders();
		Attacker attacker = game.getAttacker();

		//Chooses a random LEGAL action if required. Could be much simpler by simply returning
		//any random number of all of the ghosts
		for(int i = 0; i < actions.length; i++)
		{
			Defender defender = enemies.get(i);
			List<Integer> possibleDirs = defender.getPossibleDirs();

			if (possibleDirs.size() != 0)
			{
//				actions[i]=possibleDirs.get(Game.rng.nextInt(possibleDirs.size()));

				// ======== Chaser method ========
				if (i == 0)
				{
					actions[i] = defender.getNextDir(attacker.getLocation(), true);
				}

				//======== Power pill defender method ========
				List<Node> powerPills = game.getPowerPillList();

				if (i == 1 || i == 2)
				{
					Node closePowerPill = attacker.getTargetNode(powerPills, true);

					if (!powerPills.isEmpty())
					{
						actions[i] = defender.getNextDir(closePowerPill, true);
					}
					else
					{
						actions[i] = defender.getNextDir(attacker.getLocation(), true);
					}
				}

				// ======== Interceptor method ========
                /*if (i == 3)
                {
                    List<Node> attackerPossLoc = attacker.getPossibleLocations(false);
                    Node defenderNodeToGo = attacker.getTargetNode(attackerPossLoc, true);

                    actions[i] = defender.getNextDir(defenderNodeToGo, true);

                }*/

				// ======== Circle method ========
				if (i == 3)
				{
					final int RADIUS_TO_ATTACKER = 20;
					if (defender.getLocation().getPathDistance(attacker.getLocation()) < RADIUS_TO_ATTACKER)
					{
						if (!powerPills.isEmpty())
						{
							Node closePowerPill = defender.getTargetNode(powerPills, true);
							actions[i] = defender.getNextDir(closePowerPill, true);
						}
						else
						{
							actions[i] = defender.getNextDir(attacker.getLocation(), true);
						}
					}

					if (defender.getLocation().getPathDistance(attacker.getLocation()) > RADIUS_TO_ATTACKER)
					{
						actions[i] = defender.getNextDir(attacker.getLocation(), true);
					}
					else {
						actions[i] = defender.getNextDir(attacker.getLocation(), true);
					}
				}
			}
			else
				actions[i] = -1;
		}

		return actions;
	}
}