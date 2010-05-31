package com.fatwire.cs.spring.command.sample;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.TypeVariable;
import java.util.Iterator;
import java.util.Map;

import org.springframework.beans.BeanUtils;

import com.fatwire.cs.spring.command.Action;
import com.fatwire.cs.spring.command.ActionInjector;

public class SimpleActionInjector implements ActionInjector {

    public void inject(Action action, Map<String, Object> valueMap)
            throws Exception {
        for (Iterator<Map.Entry<String, Object>> itor = valueMap.entrySet()
                .iterator(); itor.hasNext();) {
            Map.Entry<String, Object> entry = itor.next();

            PropertyDescriptor pd = BeanUtils.getPropertyDescriptor(action
                    .getClass(), entry.getKey());
            if (pd != null) {
                Method writeMethod = pd.getWriteMethod();

                if (writeMethod != null) {
                    TypeVariable[] tv = writeMethod.getTypeParameters();
                    //TODO check for correct type
                    if (tv.length == 1)
                        writeMethod.invoke(action, new Object[] { entry
                                .getValue() });
                }
            }

        }

    }

}
