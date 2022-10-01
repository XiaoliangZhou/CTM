package util;

import CGA.problem.Problem;
import org.uma.jmetal.util.JMetalException;

import java.lang.reflect.InvocationTargetException;

public class ProblemUtils {
    public ProblemUtils() {
    }

    public static <S> Problem<S> loadProblem(String problemName) {
        try {
            Problem<S> problem = (Problem)Class.forName(problemName).getConstructor().newInstance();
            return problem;
        } catch (InstantiationException var3) {
            throw new JMetalException("newInstance() cannot instantiate (abstract class)", var3);
        } catch (IllegalAccessException var4) {
            throw new JMetalException("newInstance() is not usable (uses restriction)", var4);
        } catch (InvocationTargetException var5) {
            throw new JMetalException("an exception was thrown during the call of newInstance()", var5);
        } catch (NoSuchMethodException var6) {
            throw new JMetalException("getConstructor() was not able to find the constructor without arguments", var6);
        } catch (ClassNotFoundException var7) {
            throw new JMetalException("Class.forName() did not recognized the name of the class", var7);
        }
    }
}
