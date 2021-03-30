package ist.meic.pava.MultipleDispatch;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class UsingMultipleDispatch {
    public static Object invoke(Object receiver, String name, Object... args) {
        try {
            ArrayList<Class> objects = Arrays.stream(args).map(object ->
                    object.getClass()).collect(Collectors.toCollection(ArrayList::new));
            Method method = findBestMethod(receiver.getClass(), name, 0, objects, 0);
            return method.invoke(receiver, args);
        } catch (IllegalAccessException | InvocationTargetException |
                NoSuchMethodException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    static Method findBestMethod(Class classType, String name, int pos,
                                 ArrayList<Class> objects, int iter) throws NoSuchMethodException {
        try {
            Class[] parameters = new Class[objects.size()];
            for(int i = 0; i < objects.size(); i++) {
                parameters[i] = objects.get(i);
            }
            Method method = classType.getMethod(name, parameters);

            // if no exception thrown and there are still objects to analyze
            if(pos < objects.size() - 1) {
                return findBestMethod(classType, name, pos + 1, objects, iter + 1);
            }

            return method;
        } catch (NoSuchMethodException e) {
            if (objects.get(pos) == Object.class) {
                throw e;
            } else {
                // TODO: check
                // Idea: if method not found yet, go through positions again
                if(pos == objects.size()) {
                    return findBestMethod(classType, name, 0, objects, 0);
                }
                else {
                    if(iter % 2 != 0) {
                        Class newObject = objects.get(pos).getSuperclass();
                        objects.set(pos, newObject);
                        return findBestMethod(classType, name, pos, objects, iter + 1);
                    } else {
                        return findBestMethod(classType, name, pos + 1, objects, iter + 1);
                    }
                }
            }
        }
    }
}
