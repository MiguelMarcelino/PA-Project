package ist.meic.pava.MultipleDispatch;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UsingMultipleDispatch {
    public static Object invoke(Object receiver, String name, Object... args) {
        try {
            ArrayList<Object> objects = Arrays.stream(args).collect(Collectors.toCollection(ArrayList::new));

            Class classType = receiver.getClass();
            // TODO: Filter elements by num of parameters
            Stream<Method> methods = Arrays.stream(classType.getMethods()).
                    filter(method -> method.getName().equals(name) && method.getParameterCount() == objects.size());
            ArrayList<Method> methodList = methods.collect(Collectors.toCollection(ArrayList::new));
            Method method = findBestMethod(methodList, 0, objects, 0);
            return method.invoke(receiver, args);
        } catch (IllegalAccessException | InvocationTargetException |
                NoSuchMethodException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    static Method findBestMethod(ArrayList<Method> methodList, int pos,
                                 ArrayList<Object> objects, int bestMethodIndex) throws NoSuchMethodException {
        if(pos == methodList.size()){
            return methodList.get(bestMethodIndex);
        }

        Class[] parameterTypes = methodList.get(pos).getParameterTypes();
        Class[] currBestMethod = methodList.get(bestMethodIndex).getParameterTypes();
        boolean notInstance = false;
        int best = -1;
        for(int i = 0; i < objects.size() && !notInstance; i++) {
            if (!parameterTypes[i].isInstance(objects.get(i))) {
                notInstance = true;
            }
            if (parameterTypes[i].isInstance(currBestMethod[i])) {
                best = pos;
            }
        }

        if(notInstance) {
            return findBestMethod(methodList, pos + 1, objects, bestMethodIndex);
        } else {
            best = best>=0?best:bestMethodIndex;
            return findBestMethod(methodList, pos + 1, objects, best);
        }


//        invoke(screen,draw,circle,pencil)
//
//        Screen.draw(circle,pen) ->best bestMethodIndex
//        Screen.draw(circle,brush)
//
//        Screen.draw(circle,triangle)

//        Screen.draw(circle,crayon)
//        Screen.draw(dot,pencil)
//        Device.draw
//        Device.draw
//
//        circle->dot
//        brush->pen->pencil

    }

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
