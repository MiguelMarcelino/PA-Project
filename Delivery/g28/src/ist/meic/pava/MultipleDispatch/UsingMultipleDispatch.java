package ist.meic.pava.MultipleDispatch;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UsingMultipleDispatch {

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

            ArrayList<Method> methodList = Arrays.stream(classType.getMethods())
                    .filter(method -> method.getName().equals(name) &&
                            method.getParameterCount() == objects.size())
                    .collect(Collectors.toCollection(ArrayList::new));

            Method method = findBestMethod(methodList, objects);
            // Alternative Method (more efficient)
            // Method method = findBestMethodV2(methodList, objects, 0, -1);
            return method.invoke(receiver, args);
        } catch (IllegalAccessException | InvocationTargetException |
                NoSuchMethodException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * @param methodList - List of methods to analyze
     * @param objects    - List of parameters from function
     * @return - best method with most specific arguments
     * @throws NoSuchMethodException - in case no method is found with given info
     */
    static Method findBestMethod(ArrayList<Method> methodList, ArrayList<Object> objects)
            throws NoSuchMethodException {
        List<Method> matchingMethods = methodList.stream().filter(method -> {
            Class[] parameterTypes = method.getParameterTypes();
            for (int i = 0; i < objects.size(); i++) {
                if (!parameterTypes[i].isAssignableFrom(objects.get(i).getClass()))
                    return false;
            }
            return true;
        }).collect(Collectors.toList());

        if (matchingMethods.isEmpty()) throw new NoSuchMethodException();
        Method best = matchingMethods.get(0);

        for (Method method : matchingMethods) {
            boolean isBest = true;
            boolean isMoreSpecific = false;
            Class[] parameterTypes = method.getParameterTypes();
            Class[] currBestMethodParams = best.getParameterTypes();
            for (int i = 0; i < objects.size() && isBest  && !isMoreSpecific; i++) {
                if (currBestMethodParams[i].isAssignableFrom(parameterTypes[i])) {
                    if (currBestMethodParams[i] != parameterTypes[i])
                        isMoreSpecific = true;
                } else {
                    isBest = false;
                }
            }

            if (isMoreSpecific) best = method;
        }

        return best;
    }

    /**
     * Version 2 of the findBestMethod algorithm. Removes one iteration
     *
     * @param methodList      - List of methods to analyze
     * @param currPos         - current position of the method array
     * @param objects         - List of parameters from function
     * @param bestMethodIndex - Index of the best matching method found until a given point
     * @return - best method with most specific arguments
     * @throws NoSuchMethodException - in case no method is found with given info
     */
    static Method findBestMethodV2(ArrayList<Method> methodList, ArrayList<Object> objects,
                                   int currPos, int bestMethodIndex) throws NoSuchMethodException {
        if (currPos == methodList.size()) {
            if (bestMethodIndex == -1)
                throw new NoSuchMethodException();
            if (methodList.isEmpty())
                throw new RuntimeException("No methods provided for search");
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
                        // Classes are different, so parameterTypes[i] is more specific
                        if (currBestMethodParams[i] != parameterTypes[i])
                            moreSpecific = true;
                    } else if (!moreSpecific) {
                        bestPos = -1;
                    }
                }
            } else {
                bestPos = -1;
            }
        }

        int newBest = bestPos >= 0 ? bestPos : bestMethodIndex;
        return findBestMethodV2(methodList, objects, currPos + 1, newBest);
    }

}
