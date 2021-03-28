package ist.meic.pava.MultipleDispatch;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UsingMultipleDispatch {
    public static Object invoke(Object receiver, String name, Object args) {
        try {
            Method method = findBestMethod(receiver.getClass(), name, args.getClass());
            return method.invoke(receiver, args);
        } catch (IllegalAccessException | InvocationTargetException |
                NoSuchMethodException e) {
            throw new RuntimeException();
        }
    }

    static Method findBestMethod(Class classType, String name, Class argTypes)
            throws NoSuchMethodException {
        try {
            return classType.getMethod(name, argTypes);
        } catch (NoSuchMethodException e) {
            if (argTypes == Object.class) {
                // could not find method
                throw e;
            } else {
                return findBestMethod(classType, name, argTypes.getSuperclass());
            }
        }
    }
}
