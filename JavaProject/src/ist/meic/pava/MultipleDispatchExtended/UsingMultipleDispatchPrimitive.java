package ist.meic.pava.MultipleDispatchExtended;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UsingMultipleDispatchPrimitive {

    /**
     * @param receiver - class where to search for method
     * @param name     - name of the method to find
     * @param args     - function arguments
     * @return - object that represents the result from the invocation of the method
     */
    public static Object invoke(Object receiver, String name, Integer[] primitiveObjPositions, Object... args) {
        try {
            ArrayList<Object> objects = Arrays.stream(args).collect(Collectors.toCollection(ArrayList::new));
            Class classType = receiver.getClass();
            ArrayList<Integer> primitivePos = Arrays.stream(primitiveObjPositions)
                    .collect(Collectors.toCollection(ArrayList::new));

            // TODO: Try to change to variable arity methods
            Stream<Method> methods = Arrays.stream(classType.getMethods()).
                    filter(method -> method.getName().equals(name) && method.getParameterCount() == objects.size());

            ArrayList<Method> methodList = methods.collect(Collectors.toCollection(ArrayList::new));
            Method method = findBestMethodV2(methodList, objects, primitivePos);
            return method.invoke(receiver, args);
        } catch (IllegalAccessException | InvocationTargetException |
                NoSuchMethodException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    /**
     * @param methodList - List of methods to analyze
     * @param objects    - List of parameters from function
     * @return - best method with most specific arguments
     * @throws NoSuchMethodException - in case no method is found with given info
     */
    static Method findBestMethodV2(ArrayList<Method> methodList, ArrayList<Object> objects,
                                   ArrayList<Integer> primitivePos) throws NoSuchMethodException {
        List<Method> matchingMethods = methodList.stream().filter(method -> {
            Class[] parameterTypes = method.getParameterTypes();
            for (int i = 0; i < objects.size(); i++) {
                Class objectClass;
                if (primitivePos.contains(i)) {
                    objectClass = getPrimitiveType(objects.get(i).getClass());
                } else {
                    objectClass = objects.get(i).getClass();
                }

                if (!parameterTypes[i].isAssignableFrom(objectClass)) {
                    return false;
                }

            }
            return true;
        }).collect(Collectors.toList());

        Method best = matchingMethods.get(0);
        if (best == null) {
            throw new NoSuchMethodException();
        }

        // Does anything else need to be converted to primitive methods?
        for (Method method : matchingMethods) {
            int bestPos = 0;
            boolean isMoreSpecific = false;
            Class[] parameterTypes = method.getParameterTypes();
            Class[] currBestMethodParams = best.getParameterTypes();
            for (int i = 0; i < objects.size() && (bestPos >= 0 && !isMoreSpecific); i++) {
                if (currBestMethodParams[i].isAssignableFrom(parameterTypes[i])) {
                    if (currBestMethodParams[i] != parameterTypes[i]) {
                        bestPos = -1;
                    }
                } else {
                    isMoreSpecific = true;
                }
            }

            if (bestPos == -1) {
                best = method;
            }
        }

        return best;
    }

    /**
     * Gets primitive type from Class and returns it
     *
     * @param c - class to get primitive type from
     * @return primitive type from class c
     */
    private static Class getPrimitiveType(Class c) {
        if (!c.isPrimitive()) {
            throw new RuntimeException("No such primitive type");
        }

        if (c.isInstance(Integer.class)) {
            return int.class;
        } else if (c.isInstance(Boolean.class)) {
            return boolean.class;
        } else if (c.isInstance(Character.class)) {
            return char.class;
        } else if (c.isInstance(Byte.class)) {
            return byte.class;
        } else if (c.isInstance(Float.class)) {
            return float.class;
        } else if (c.isInstance(Short.class)) {
            return short.class;
        } else if (c.isInstance(Double.class)) {
            return double.class;
        } else if (c.isInstance(Long.class)) {
            return long.class;
        } else {
            throw new RuntimeException("An error occurred");
        }
    }
}
