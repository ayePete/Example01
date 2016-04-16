import java.util.ArrayList;
import java.util.List;

import ilog.concert.*;
import ilog.cplex.*;

public class Example_1 {
	public static void solveMe() {
		try {
			IloCplex cplex = new IloCplex();

			// variables
			IloNumVar x = cplex.numVar(0, Double.MAX_VALUE, "x");
			IloNumVar y = cplex.numVar(0, Double.MAX_VALUE, "y");

			// expressions
			IloLinearNumExpr objective = cplex.linearNumExpr();
			objective.addTerm(0.12, x);
			objective.addTerm(0.15, y);

			// define objective
			cplex.addMinimize(objective);

			// define constraints
			List<IloRange> constraints = new ArrayList<IloRange>();
			constraints.add(cplex.addGe(cplex.sum(cplex.prod(60, x), cplex.prod(60, y)), 300));
			constraints.add(cplex.addGe(cplex.sum(cplex.prod(12, x), cplex.prod(6, y)), 36));
			constraints.add(cplex.addGe(cplex.sum(cplex.prod(10, x), cplex.prod(30, y)), 90));

			IloLinearNumExpr numExpr = cplex.linearNumExpr();
			numExpr.addTerm(2, x);
			numExpr.addTerm(-1, y);
			constraints.add(cplex.addEq(numExpr, 0));

			numExpr = cplex.linearNumExpr();
			numExpr.addTerm(1, y);
			numExpr.addTerm(-1, x);
			constraints.add(cplex.addLe(numExpr, 8));
			
			cplex.setParam(IloCplex.IntParam.SimDisplay.SimDisplay, 0);

			// solve
			if (cplex.solve()) {
				System.out.println("Obj = " + cplex.getObjValue());
				System.out.println("x = " + cplex.getValue(x));
				System.out.println("y = " + cplex.getValue(y));

				for (int i = 0; i < constraints.size(); i++) {
					System.out.println("Dual constraint " + (i + 1) + " " + cplex.getDual(constraints.get(i)));
					System.out.println("Slack constraint " + (i + 1) + " " + cplex.getSlack(constraints.get(i)));
				}

			} else {
				System.out.println("Model not solved");
			}

			cplex.end();
		} catch (IloException e) {
			e.printStackTrace();
		}
	}

}
