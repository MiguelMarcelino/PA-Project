package ist.meic.pava.MultipleDispatchExtended;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UsingMultipleDispatchInterface {

    /**
     * @param receiver - class where to search for method
     * @param name     - name of the method to find
     * @param args     - function arguments
     * @return - object that represents the result from the invocation of the method
     */
    public static Object invoke(Object receiver, String name, Object... args) {
        try {
            ArrayList<Object> objects = Arrays.stream(args).collect(Collectors.toCollection(ArrayList::new));
            Class classType = receiver.getClass();

            // TODO: Try to change to variable arity methods
            Stream<Method> methods = Arrays.stream(classType.getMethods()).
                    filter(method -> method.getName().equals(name) && method.getParameterCount() == objects.size());

            ArrayList<Method> methodList = methods.collect(Collectors.toCollection(ArrayList::new));
            Method method = findBestMethod(methodList, objects, 0, -1);
            return method.invoke(receiver, args);
        } catch (IllegalAccessException | InvocationTargetException |
                NoSuchMethodException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    /**
     * @param methodList      - List of methods to analyze
     * @param currPos         - current position of the method array
     * @param objects         - List of parameters from function
     * @param bestMethodIndex - Index of the best matching method found until a given point
     * @return - best method with most specific arguments
     * @throws NoSuchMethodException - in case no method is found with given info
     */
    static Method findBestMethod(ArrayList<Method> methodList, ArrayList<Object> objects,
                                 int currPos, int bestMethodIndex) throws NoSuchMethodException {
        if (currPos == methodList.size()) {
            if (bestMethodIndex == -1 || methodList.size() == 0) {
                throw new NoSuchMethodException();
            }
            return methodList.get(bestMethodIndex);
        }

        Class[] parameterTypes = methodList.get(currPos).getParameterTypes();
        int bestPos = currPos;
        boolean moreSpecific = false;
        for (int i = 0; i < objects.size() && bestPos >= 0; i++) {
            if (parameterTypes[i].isAssignableFrom(objects.get(i).getClass())) {
                // case where best method was not found yet
                if (bestMethodIndex >= 0) {
                    Class[] currBestMethodParams = methodList.get(bestMethodIndex).getParameterTypes();

                    if (currBestMethodParams[i].isAssignableFrom(parameterTypes[i])) {
                        bestPos = currPos;
                        // parameter being analyzed is more specific than parameter from best Method
                        // in position i
                        if(currBestMethodParams[i].isAssignableFrom(parameterTypes[i].getSuperclass())) {
                            moreSpecific = true;
                        }
                    } else if(!moreSpecific){
                        bestPos = -1;
                    }
                }
            } else {
                bestPos = -1;
            }
        }

        int newBest = bestPos >= 0 ? bestPos : bestMethodIndex;
        return findBestMethod(methodList, objects, currPos + 1, newBest);
    }
}
