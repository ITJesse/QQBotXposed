package com.itjesse.qqbot;

import java.lang.reflect.Field;

/**
 * Created by Jesse on 16/2/16.
 */
public class utils {
    public static Field changAccessory(Class paramClass1, Class paramClass2, String paramString) {
        Object localObject1 = paramClass1;
        Object localObject2;
        do {
            Field[] localObject3 = ((Class) localObject1).getDeclaredFields();
            int j = localObject3.length;
            int i = 0;
            while (i < j) {
                Field localField = localObject3[i];
                if ((localField.getType() == paramClass2) && (localField.getName().equals(paramString))) {
                    localField.setAccessible(true);
                    return localField;
                }
                i += 1;
            }
            localObject2 = ((Class) localObject1).getSuperclass();
            localObject1 = localObject2;
        } while (localObject2 != null);
        throw new NoSuchFieldError("Field of type " + paramClass2.getName() + " in class " + paramClass1.getName());
    }
}
