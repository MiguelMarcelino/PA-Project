package ist.meic.pava.MultipleDispatch;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
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
        for (int i = 0; i < objects.size() && bestPos >= 0; i++) {
            if (parameterTypes[i].isAssignableFrom(objects.get(i).getClass())) {
                // case where best method was not found yet
                if (bestMethodIndex >= 0) {
                    Class[] currBestMethod = methodList.get(bestMethodIndex).getParameterTypes();
                    if (currBestMethod[i].isAssignableFrom(parameterTypes[i])) {
                        bestPos = currPos;
                    } else {
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

    ///////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////// Old functions ////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

//    /**
//     * @param methodList
//     * @param currPos
//     * @param objects
//     * @param bestMethodIndex
//     * @return
//     * @throws NoSuchMethodException
//     */
//    static Method findBestMethod(ArrayList<Method> methodList, int currPos,
//                                 ArrayList<Object> objects, int bestMethodIndex) throws NoSuchMethodException {
//        if (currPos == methodList.size()) {
//            if (bestMethodIndex == -1 || methodList.size() == 0) {
//                throw new NoSuchMethodException();
//            }
//            return methodList.get(bestMethodIndex);
//        }
//
//        Class[] parameterTypes = methodList.get(currPos).getParameterTypes();
//        boolean isInstance = true;
//        int best = -1;
//        for (int i = 0; i < objects.size() && isInstance; i++) {
//            if (parameterTypes[i].isAssignableFrom(objects.get(i).getClass())) {
//                // case where best method was not found yet
//                if (bestMethodIndex >= 0) {
//                    Class[] currBestMethod = methodList.get(bestMethodIndex).getParameterTypes();
//                    if (currBestMethod[i].isAssignableFrom(parameterTypes[i])) {
//                        best = currPos;
//                    } else {
//                        isInstance = false;
//                    }
//                } else {
//                    best = currPos;
//                }
//            } else {
//                isInstance = false;
//            }
//        }
//
//        if (!isInstance) {
//            return findBestMethod(methodList, currPos + 1, objects, bestMethodIndex);
//        }
//
//        int newBest = best >= 0 ? best : bestMethodIndex;
//        return findBestMethod(methodList, currPos + 1, objects, newBest);
//    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////// Shame ////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

//    static Method findBestMethod(Class classType, String name, int pos,
//                                 ArrayList<Class> objects, int iter) throws NoSuchMethodException {
//        try {
//            Class[] parameters = new Class[objects.size()];
//            for(int i = 0; i < objects.size(); i++) {
//                parameters[i] = objects.get(i);
//            }
//            Method method = classType.getMethod(name, parameters);
//
//            // if no exception thrown and there are still objects to analyze
//            if(pos < objects.size() - 1) {
//                return findBestMethod(classType, name, pos + 1, objects, iter + 1);
//            }
//
//            return method;
//        } catch (NoSuchMethodException e) {
//            if (objects.get(pos) == Object.class) {
//                throw e;
//            } else {
//                // TODO: check
//                // Idea: if method not found yet, go through positions again
//                if(pos == objects.size()) {
//                    return findBestMethod(classType, name, 0, objects, 0);
//                }
//                else {
//                    if(iter % 2 != 0) {
//                        Class newObject = objects.get(pos).getSuperclass();
//                        objects.set(pos, newObject);
//                        return findBestMethod(classType, name, pos, objects, iter + 1);
//                    } else {
//                        return findBestMethod(classType, name, pos + 1, objects, iter + 1);
//                    }
//                }
//            }
//        }
//    }
}
