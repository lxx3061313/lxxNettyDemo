
package com.lxx.mydemo.nettydemo.service.common.web;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Throwables;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.lxx.mydemo.nettydemo.service.common.util.Safes;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;
import sun.misc.Unsafe;

/**
 * Description: MConfigDriver - A All Annotation Based Static Config Way
 *
 * Improve qConfig supported in Biz System. Please take care of and little patient to read the note below.
 * Do not use the fucking old way to load your springconfig file. That is not wise. And is opinionated. And I thing u will not.
 *
 * <p> Normal Value In the Properties Value u Placed in the files that was define in the "PlaceHolderConfigurer"
 * 1. add ur normal springconfig in resource.* directory
 * 2. add code below to you class
 * {code}
 * MValue("propertiesKeys")
 * private String myProfile
 * {code}
 *
 * <p> All Annotation Based MConfig Dynamic Config
 *
 * 1. add ur springconfig in qconfig.corp.qunar.com -> qta.orderbiz -> prod -> profile
 * 2. add code below to your class
 * !important you class need to be manage by Spring IOC which means it should be a Spring bean.
 * {code}
 * MValue("propertiesKeys")
 * private String myProfile
 * {code}
 *
 * !!! important : we only support primitive types such as int, float ... ,
 * And some basic type such as String, BigDecimal, Money
 *
 * <p>Do you always think Annotation Based Configuration is better than code ?
 *
 * DEFINITELY NOT.
 * The introduction of annotation-based configurations raised the question of whether this approach
 * is better than XML or Java Code. The short answer is it depends.
 * The long answer is that each approach has its pros and cons,
 * and usually it is up to the developer to decide which strategy suits them better.
 * Due to the way they are defined, annotations provide a lot of context in their declaration,
 * leading to shorter and more concise configuration. However,
 * XML excels at wiring up components without touching their source code or recompiling them.
 * Some developers prefer having the wiring close to the source while others argue that
 * annotated classes are no longer POJOs and, furthermore,
 * that the configuration becomes decentralized and harder to control.
 *
 * BUT DEFINITELY YEP to CONFIG SOMETHING in particular.
 *
 * Reference:
 * 1. wiki - How to Use MConfig @see http://wiki.corp.qunar.com/x/GgTFAw
 *
 * Rick
 * 1. 64-bit value may not safe when replacing value in such a short time --> so i don't like to support double and long
 * 2. restart the container when changing the qConfig profile may lead to this not work
 * 3. may not work well with final and static field. Please see the method setUnsafeValue()
 *
 * Performance
 * Loading time weaving. So time cost ignore.
 * In general, there's no need to perform such immediately when u change ur profile in qConfig
 *
 * important !!! this is not work if the dynamic field is inherit from parent class
 *
 * @author yushen.ma
 * @version 2015-04-24 12:16
 * @see //http://docs.oracle.com/javase/specs/jls/se8/html/jls-17.html#jls-17.7
 * @see MValue
 * @see BeanPostProcessor
 * @see BeanFactoryPostProcessor
 * @see PropertyPlaceholderConfigurer
 * @see sun.reflect.Reflection
 * @see Unsafe
 */
public class MConfigDriver extends PropertyPlaceholderConfigurer implements BeanPostProcessor,
        ApplicationListener<ContextRefreshedEvent>, PriorityOrdered {

    /** a simple dynamic properties callback container */
    private final Multimap<String, Function<String, Void>> dynamicProperties = ArrayListMultimap.create();

    /** define the suffix of and property file */
    private final String Q_PROPERTY_FILE_SUFFIX = ".properties";

    /** define the default MConfig file */
    private final String DEFAULT_Q_CONFIG_FILE = "profile" + Q_PROPERTY_FILE_SUFFIX;

    /** you can add some other MConfig file via add a String of file name*/
    private final Set<String> MConfigLocations = Sets.newHashSet(DEFAULT_Q_CONFIG_FILE);

    private final Logger logger = LoggerFactory.getLogger(getClass());

    /** a simple init lock */
    private final AtomicBoolean init = new AtomicBoolean(false);

    /** hacking to all the properties load by PropertyPlaceHolderConfigurer */
    private static Map<String, String> localPropertiesMap = Maps.newHashMap();

    /** simple as default */
    private int springSystemPropertiesMode = SYSTEM_PROPERTIES_MODE_FALLBACK;

    /** register the dynamic hacking to qConfig hook */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (init.get() || !init.compareAndSet(false, true)){  return;} // in case of something I'm not ware of
        logger.info("MConfig-Driven Initialization Dynamic Hacking Start ...");
        // register dynamic reload
        Safes.of(this.getMConfigLocations()).stream()
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.toList())
                .forEach(configName -> {
                    if (StringUtils.isBlank(configName)){ return;}
                    //MapConfig config = MapConfig.get(configName);
                    logger.debug("MConfig-Driven load {}", configName);
                    //Preconditions.checkArgument(null != config,
                      //      MessageFormat.format("MConfig-Driven there is no file named {0} is placed in qConfig", configName));
//                    config.addListener(conf -> {
//                        if (MapUtils.isEmpty(conf)){
//                            return;
//                        }
//                        Safes.of(conf.entrySet()).forEach(item -> {
//                                if (null == item) {
//                                    return;
//                                };
//                                Safes.of(dynamicProperties.get(item.getKey()))
//                                        .forEach(function -> {
//                                        if (null == function) {
//                                            return;
//                                        }
//                                        function.apply(item.getValue());
//                                    });
//                        });
//                    });
                });
    }

    /** init the default properties in field  */
    @Override
    public Object postProcessBeforeInitialization(final Object bean, final String beanName)
            throws BeansException {
        List<Field> fields = Lists.newArrayList(bean.getClass().getDeclaredFields());
        Safes.of(fields).stream()
                .filter(input -> input != null && input.isAnnotationPresent(MValue.class))
                .forEach(input -> {
                    if (null == input) {
                        return;
                    }
                    String property = findAnyProperties(input.getAnnotation(MValue.class).value());
                    unsafeSetValue(bean, input, property);
                });
        return bean;
    }

    /** generate dynamic callback to the map */
    @Override
    public Object postProcessAfterInitialization(final Object bean, String beanName)
            throws BeansException {
        List<Field> fields = Lists.newArrayList(bean.getClass().getDeclaredFields());
        Safes.of(fields).stream()
                .filter(input -> null != input && input.isAnnotationPresent(MValue.class))
                .forEach(input -> {
                    if (null == input) {
                        return;
                    }
                    addDynamicProperties(bean, input);
                });
        return bean;
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE - 2;
    }

    @Override
    public void setSystemPropertiesMode(int systemPropertiesMode) {
        super.setSystemPropertiesMode(systemPropertiesMode);
        springSystemPropertiesMode = systemPropertiesMode;
    }

    @Override
    protected void processProperties(ConfigurableListableBeanFactory beanFactory, Properties props) throws BeansException {
        super.processProperties(beanFactory, props);
        for (Object key : props.keySet()) {
            String keyStr = key.toString();
            String valueStr = resolvePlaceholder(keyStr, props, springSystemPropertiesMode);
            localPropertiesMap.put(keyStr, valueStr);
        }
    }

    @Override
    protected Properties mergeProperties() throws IOException {
        Properties properties = super.mergeProperties();
//        getMConfigLocations()
//                .stream()
//                .filter(item -> MapConfig.get(item) != null)
//                .forEach(item -> properties.putAll(MapConfig.get(item).asMap())
//                );
        return properties;

    }

    public Set<String> getMConfigLocations() {
        return Safes.of(MConfigLocations);
    }

    public void setMConfigLocations(final Set<String> MConfigLocations) {
        if (CollectionUtils.isEmpty(MConfigLocations)) {
            return;
        }
        Iterables.removeIf(MConfigLocations, input -> StringUtils.isBlank(input) || !input.endsWith(Q_PROPERTY_FILE_SUFFIX));
        this.MConfigLocations.addAll(MConfigLocations);
    }

    /**
     * find the properties from the given key in all the properties
     *
     * add to system properties support and local file support
     *
     * @param key given key
     * @return value of the key defined in property file
     */
    String findAnyProperties(String key) {
        // 1. find in qConfig
        for (String configName : this.getMConfigLocations()) {
            //String s = MapConfig.get(configName).asMap().get(key);
            //if (s != null) {return s;} // something. it's just a blank
        }//End Of For

        // 2. find in system springconfig
        String s = super.resolveSystemProperty(key);
        if (null != s) {return s;}

        // 3. find in local properties
        s = localPropertiesMap.get(key);
        if (null != s) {return s;}
        throw new IllegalArgumentException(key + " is not found in all the Config files (local, system, qconfig)"); // fall fast
    }

    /**
     * wrapper bean and field to a callback function and add to it the target list
     * @param bean bean
     * @param field field
     */
    void addDynamicProperties(Object bean, final Field field) {
        // wrapper to weak reference in case of memory over flow
        final WeakReference<Object> beanWeakReference = new WeakReference<>(bean);
        // when u suddenly found the power of function programming. U would fuck JAVA every day
        this.dynamicProperties.put(findMValueKey(field), new Lamb<String>() {
            @Override
            public void apply0(String input) {
                final Object bean = beanWeakReference.get();
                if (bean == null) {return;} // which means bean is collected by GC
                assert field != null;
                if (isTypeNotMatch(bean, field, input)) {return;} // in case u brain fucked
                unsafeSetValue(bean, field, input);
                logger.warn("MConfig-Driven Reload finish. object: {}, field: {}, key: {}, value: {}",
                        bean.getClass().getSimpleName(), field.getName(), findMValueKey(field), input);
            }
        });
    }

    /**
     * check If the given value is match to the type of the field
     * @param bean target bean
     * @param field target field
     * @param input given value
     * @return boolean || and some side effect of print not match error log
     */
    boolean isTypeNotMatch(final Object bean, final Field field, final String input) {
        try {
            QPropertiesEditor.INSTANCE.apply(input, field.getType());
        } catch (Throwable t) {
            logger.error("MConfig-Driven Reload Error." +
                            "\n Type is not match object: {}, field: {}, key: {}, value: {}." +
                            "\n So I skip it. Please check and reload. it won't effect anything now",
                    bean.getClass().getSimpleName(), field.getName(), findMValueKey(field), input);
            return true;
        }
        return false;
    }

    /**
     * find the property key from the given field
     * @param field given field
     * @return target property key
     */
    String findMValueKey(Field field) {
        Preconditions.checkArgument(field.isAnnotationPresent(MValue.class));
        MValue annotation = field.getAnnotation(MValue.class);
        return annotation.value();
    }

    /**
     * unsafe set value to a object's field
     * 1. convert String to Correct value
     * 2. safely set value to object in multi-thread env !!!! which means not support for long and double
     *
     * <p>If the underlying field is static, the {@code obj} argument is
     * ignored; it may be null.
     *
     * <p>Otherwise the underlying field is an instance field.  If the
     * specified object argument is null, the method throws a
     * {@code NullPointerException}.  If the specified object argument is not
     * an instance of the class or interface declaring the underlying
     * field, the method throws an {@code IllegalArgumentException}.
     *
     * <p>If this {@code Field} object enforces Java language access control, and
     * the underlying field is inaccessible, the method throws an
     * {@code IllegalAccessException}.
     *
     * <p>If the underlying field is final, the method throws an
     * {@code IllegalAccessException} unless
     * {@code setAccessible(true)} has succeeded for this field
     * and this field is non-static. Setting a final field in this way
     * is meaningful only during deserialization or reconstruction of
     * instances of classes with blank final fields, before they are
     * made available for access by other parts of a program. Use in
     * any other context may have unpredictable effects, including cases
     * in which other parts of a program continue to use the original
     * value of this field.
     *
     * <p>If the underlying field is of a primitive type, an unwrapping
     * conversion is attempted to convert the new value to a value of
     * a primitive type.  If this attempt fails, the method throws an
     * {@code IllegalArgumentException}.
     *
     * <p>If, after possible unwrapping, the new value cannot be
     * converted to the type of the underlying field by an identity or
     * widening conversion, the method throws an
     * {@code IllegalArgumentException}.
     *
     * <p>If the underlying field is static, the class that declared the
     * field is initialized if it has not already been initialized.
     *
     * <p>The field is set to the possibly unwrapped and widened new value.
     *
     * <p>If the field is hidden in the type of {@code obj},
     * the field's value is set according to the preceding rules.
     *
     * @param target target object
     * @param field target field
     * @param value given value
     */
    void unsafeSetValue(Object target, Field field, String value) {
        boolean accessible = field.isAccessible();
        field.setAccessible(true);
        try {
            field.set(target, QPropertiesEditor.INSTANCE.apply(value, field.getType()));
            logger.debug("MConfig-Driven set bean : {}, field : {}, value : {}",
                    target.getClass().getSimpleName(), field.getName(), value);
        } catch (Throwable ignore) { // ignore
            logger.error("MConfig-Driven dynamic reload qConfig error. Please check the bean:{}, field: {}",
                    target.getClass().getSimpleName(), field.getName(), ignore);
            throw Throwables.propagate(ignore);
        } finally { // fix accessible
            field.setAccessible(accessible);
        }

    }

}

class $<T> implements Iterable<T> {

    final private FluentIterable<T> target;

    private $(Iterable<T> origin) {
        this.target = FluentIterable.from(origin);
    }

    public static <T> $<T> create(Iterable<T> target) {
        if (Iterables.isEmpty(target)) {
            return new $<>(new ArrayList<T>());
        }
        return new $<>(target);
    }

    public $<T> filter(Predicate<T> predicate) {
        target.filter(predicate);
        return this;
    }

    public <V> ImmutableMap<T, V> map(Function<? super T, V> valueFunction) {
        return target.toMap(valueFunction);
    }

    public $<T> each(Function<T, Void> function) {
        checkNotNull(function);
        for (T item : target) {function.apply(item);}
        return this;
    }

    public Iterable<T> value() {
        return target;
    }

    @Override
    public Iterator<T> iterator() {
        return this.target.iterator();
    }

    public List<T> list() {
        return this.target.toList();
    }
}

abstract class Lamb<F> implements Function<F, Void> {

    public abstract void apply0(F input);

    @Override
    public Void apply(F input) {
        if (skipNull() && testNull(input)) {
            return null;
        }
        apply0(input);
        return null;
    }

    // default to skip NULL value
    protected boolean skipNull() {
        return true;
    }

    // declare the null test method
    protected boolean testNull(F input) {
        return null == input;
    }

    /**
     * This class inherits equals(Object) from an abstract superclass,
     * and hashCode() from java.lang.Object (which returns the identity hash code,
     * an arbitrary value assigned to the object by the VM).
     * Therefore, the class is very likely to violate the invariant that equal objects must have equal hashcodes.
     *
     * If you don't want to define a hashCode method,
     * and/or don't believe the object will ever be put into a HashMap/Hashtable,
     * define the hashCode() method to throw UnsupportedOperationException.
     */

    @Override
    public boolean equals(Object object) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int hashCode() {
        throw new UnsupportedOperationException();
    }

}