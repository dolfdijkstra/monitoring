package com.fatwire.cs.spring.command;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.fatwire.cs.spring.command.sample.NullAction;

public class SpringBeanActionFactory implements ActionFactory,
        ApplicationContextAware {
    private ApplicationContext applicationContext;

    private String prefix;

    private String suffix;

    public Action createAction(String key) {
        if (key == null)
            return new NullAction();
        Object o = applicationContext.getBean(getActionBeanName(key));
        if (o instanceof Action) {
            return (Action) o;
        } else {
            throw new IllegalArgumentException(key
                    + "does not implement Action.");
        }
    }

    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        this.applicationContext = applicationContext;

    }

    protected String getActionBeanName(String key) {

        if (prefix == null && suffix == null) {
            return key;
        }
        StringBuilder sb = new StringBuilder();
        if (prefix != null) {
            sb.append(prefix);
        }
        sb.append(key);
        if (suffix != null) {
            sb.append(suffix);
        }
        return sb.toString();

    }

    /**
     * @return the beanNamePostfix
     */
    public String getSuffix() {
        return suffix;
    }

    /**
     * @param beanNamePostfix the beanNamePostfix to set
     */
    public void setSuffix(String beanNamePostfix) {
        this.suffix = beanNamePostfix;
    }

    /**
     * @return the beanNamePrefix
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * @param beanNamePrefix the beanNamePrefix to set
     */
    public void setPrefix(String beanNamePrefix) {
        this.prefix = beanNamePrefix;
    }

}
