package com.fatwire.cs.spring.command.bind;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.PropertyEditorRegistrar;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingErrorProcessor;
import org.springframework.validation.MessageCodesResolver;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.mvc.AbstractCommandController;
import org.springframework.web.servlet.mvc.AbstractFormController;
import org.springframework.web.servlet.mvc.AbstractWizardFormController;
import org.springframework.web.servlet.mvc.SimpleFormController;

import com.fatwire.cs.spring.command.Action;

/**
 * <p>Controller implementation which creates an object (the command object) on
 * receipt of a request and attempts to populate this object with request parameters.</p>
 *
 * <p>This controller is the base for all controllers wishing to populate
 * JavaBeans based on request parameters, validate the content of such
 * JavaBeans using {@link org.springframework.validation.Validator Validators}
 * and use custom editors (in the form of
 * {@link java.beans.PropertyEditor PropertyEditors}) to transform 
 * objects into strings and vice versa, for example. Three notions are mentioned here:</p>
 *
 * <p><b>Command class:</b><br>
 * An instance of the command class will be created for each request and populated
 * with request parameters. A command class can basically be any Java class; the only
 * requirement is a no-arg constructor. The command class should preferably be a
 * JavaBean in order to be able to populate bean properties with request parameters.</p>
 *
 * <p><b>Populating using request parameters and PropertyEditors:</b><br>
 * Upon receiving a request, any BaseCommandController will attempt to fill the
 * command object using the request parameters. This is done using the typical
 * and well-known JavaBeans property notation. When a request parameter named
 * <code>'firstName'</code> exists, the framework will attempt to call 
 * <code>setFirstName([value])</code> passing the value of the parameter. Nested properties
 * are of course supported. For instance a parameter named <code>'address.city'</code>
 * will result in a <code>getAddress().setCity([value])</code> call on the
 * command class.</p>
 *
 * <p>It's important to realise that you are not limited to String arguments in
 * your JavaBeans. Using the PropertyEditor-notion as supplied by the
 * java.beans package, you will be able to transform Strings to Objects and
 * the other way around. For instance <code>setLocale(Locale loc)</code> is
 * perfectly possible for a request parameter named <code>locale</code> having
 * a value of <code>en</code>, as long as you register the appropriate
 * PropertyEditor in the Controller (see {@link #initBinder initBinder()}
 * for more information on that matter.</p>
 *
 * <p><b>Validators:</b>
 * After the controller has successfully populated the command object with
 * parameters from the request, it will use any configured validators to
 * validate the object. Validation results will be put in a
 * {@link org.springframework.validation.Errors Errors} object which can be
 * used in a View to render any input problems.</p>
 *
 * <p><b><a name="workflow">Workflow
 * (<a href="AbstractController.html#workflow">and that defined by superclass</a>):</b><br>
 * Since this class is an abstract base class for more specific implementation,
 * it does not override the handleRequestInternal() method and also has no
 * actual workflow. Implementing classes like
 * {@link AbstractFormController AbstractFormController},
 * {@link AbstractCommandController AbstractcommandController},
 * {@link SimpleFormController SimpleFormController} and
 * {@link AbstractWizardFormController AbstractWizardFormController}
 * provide actual functionality and workflow.
 * More information on workflow performed by superclasses can be found
 * <a href="AbstractController.html#workflow">here</a>.</p>
 *
 * <p><b><a name="config">Exposed configuration properties</a>
 * (<a href="AbstractController.html#config">and those defined by superclass</a>):</b><br>
 * <table border="1">
 *  <tr>
 *      <td><b>name</b></th>
 *      <td><b>default</b></td>
 *      <td><b>description</b></td>
 *  </tr>
 *  <tr>
 *      <td>validators</td>
 *      <td><i>null</i></td>
 *      <td>Array of Validator beans. The validator will be called at appropriate
 *          places in the workflow of subclasses (have a look at those for more info)
 *          to validate the command object.</td>
 *  </tr>
 *  <tr>
 *      <td>validator</td>
 *      <td><i>null</i></td>
 *      <td>Short-form property for setting only one Validator bean (usually passed in
 *          using a &lt;ref bean="beanId"/&gt; property.</td>
 *  </tr>
 *  <tr>
 *      <td>validateOnBinding</td>
 *      <td>true</td>
 *      <td>Indicates whether or not to validate the command object after the
 *          object has been populated with request parameters.</td>
 *  </tr>
 * </table>
 * </p>
 *
 */


public class ActionRequestBinderImpl implements ActionRequestBinder {

    private Validator[] validators;

    private boolean validateOnBinding = true;

    private MessageCodesResolver messageCodesResolver;

    private BindingErrorProcessor bindingErrorProcessor;

    private PropertyEditorRegistrar[] propertyEditorRegistrars;


    /**
     * Set the primary Validator for this binder. The Validator
     * must support the specified action class. If there are one
     * or more existing validators set already when this method is
     * called, only the specified validator will be kept. Use
     * {@link #setValidators(Validator[])} to set multiple validators.
     */
    public final void setValidator(Validator validator) {
        this.validators = new Validator[] { validator };
    }

    /**
     * Return the primary Validator for this binder.
     */
    public final Validator getValidator() {
        return (this.validators != null && this.validators.length > 0 ? this.validators[0]
                : null);
    }

    /**
     * Set the Validators for this binder.
     * The Validator must support the specified command class.
     */
    public final void setValidators(Validator[] validators) {
        this.validators = validators;
    }

    /**
     * Return the Validators for this controller.
     */
    public final Validator[] getValidators() {
        return validators;
    }

    /**
     * Set if the Validator should get applied when binding.
     */
    public final void setValidateOnBinding(boolean validateOnBinding) {
        this.validateOnBinding = validateOnBinding;
    }

    /**
     * Return if the Validator should get applied when binding.
     */
    public final boolean isValidateOnBinding() {
        return validateOnBinding;
    }

    /**
     * Set the strategy to use for resolving errors into message codes.
     * Applies the given strategy to all data binders used by this controller.
     * <p>Default is <code>null</code>, i.e. using the default strategy of
     * the data binder.
     * @see #createBinder
     * @see org.springframework.validation.DataBinder#setMessageCodesResolver
     */
    public final void setMessageCodesResolver(
            MessageCodesResolver messageCodesResolver) {
        this.messageCodesResolver = messageCodesResolver;
    }

    /**
     * Return the strategy to use for resolving errors into message codes.
     */
    public final MessageCodesResolver getMessageCodesResolver() {
        return messageCodesResolver;
    }

    /**
     * Set the strategy to use for processing binding errors, that is,
     * required field errors and <code>PropertyAccessException</code>s.
     * <p>Default is <code>null</code>, that is, using the default strategy
     * of the data binder.
     * @see #createBinder
     * @see org.springframework.validation.DataBinder#setBindingErrorProcessor
     */
    public final void setBindingErrorProcessor(
            BindingErrorProcessor bindingErrorProcessor) {
        this.bindingErrorProcessor = bindingErrorProcessor;
    }

    /**
     * Return the strategy to use for processing binding errors.
     */
    public final BindingErrorProcessor getBindingErrorProcessor() {
        return bindingErrorProcessor;
    }

    /**
     * Specify a single PropertyEditorRegistrar to be applied
     * to every DataBinder that this controller uses.
     * <p>Allows for factoring out the registration of PropertyEditors
     * to separate objects, as an alternative to {@link #initBinder}.
     * @see #initBinder
     */
    public final void setPropertyEditorRegistrar(
            PropertyEditorRegistrar propertyEditorRegistrar) {
        this.propertyEditorRegistrars = new PropertyEditorRegistrar[] { propertyEditorRegistrar };
    }

    /**
     * Specify multiple PropertyEditorRegistrars to be applied
     * to every DataBinder that this controller uses.
     * <p>Allows for factoring out the registration of PropertyEditors
     * to separate objects, as an alternative to {@link #initBinder}.
     * @see #initBinder
     */
    public final void setPropertyEditorRegistrars(
            PropertyEditorRegistrar[] propertyEditorRegistrars) {
        this.propertyEditorRegistrars = propertyEditorRegistrars;
    }

    /**
     * Return the PropertyEditorRegistrars to be applied
     * to every DataBinder that this controller uses.
     */
    public final PropertyEditorRegistrar[] getPropertyEditorRegistrars() {
        return propertyEditorRegistrars;
    }

    //    protected void initApplicationContext() {
    //        if (this.validators != null) {
    //            for (int i = 0; i < this.validators.length; i++) {
    //                if (this.commandClass != null && !this.validators[i].supports(this.commandClass))
    //                    throw new IllegalArgumentException("Validator [" + this.validators[i] +
    //                            "] does not support command class [" +
    //                            this.commandClass.getName() + "]");
    //            }
    //        }
    //    }



    /* (non-Javadoc)
     * @see com.fatwire.cs.spring.command.sample.ActionRequestBinder#bindAndValidate(javax.servlet.http.HttpServletRequest, com.fatwire.cs.spring.command.Action, java.lang.String)
     */
    public final ServletRequestDataBinder bindAndValidate(
            HttpServletRequest request, Action action, String commandName) throws Exception {

        ServletRequestDataBinder binder = createBinder(request, action,commandName);
        BindException errors = new BindException(binder.getBindingResult());
        if (!suppressBinding(request)) {
            binder.bind(request);
            onBind(request, action, errors);
            if (this.validators != null && isValidateOnBinding()
                    && !suppressValidation(request, action)) {
                for (int i = 0; i < this.validators.length; i++) {
                    ValidationUtils.invokeValidator(this.validators[i], action,
                            errors);
                }
            }
            onBindAndValidate(request, action, errors);
        }
        return binder;
    }

    /**
     * Return whether to suppress binding for the given request.
     * <p>The default implementation always returns "false". Can be overridden
     * in subclasses to suppress validation, for example, if a special
     * request parameter is set.
     * @param request current HTTP request
     * @return whether to suppress binding for the given request
     * @see #suppressValidation
     */
    protected boolean suppressBinding(HttpServletRequest request) {
        return false;
    }

    /**
     * Create a new binder instance for the given command and request.
     * <p>Called by {@link #bindAndValidate}. Can be overridden to plug in
     * custom ServletRequestDataBinder instances.
     * <p>The default implementation creates a standard ServletRequestDataBinder
     * and invokes {@link #prepareBinder} and {@link #initBinder}.
     * <p>Note that neither {@link #prepareBinder} nor {@link #initBinder} will
     * be invoked automatically if you override this method! Call those methods
     * at appropriate points of your overridden method.
     * @param request current HTTP request
     * @param command the command to bind onto
     * @return the new binder instance
     * @throws Exception in case of invalid state or arguments
     * @see #bindAndValidate
     * @see #prepareBinder
     * @see #initBinder
     */
    protected ServletRequestDataBinder createBinder(HttpServletRequest request,
            Action action, String commandName) throws Exception {

        ServletRequestDataBinder binder = new ServletRequestDataBinder(action,
                commandName);
        prepareBinder(binder);
        initBinder(request, binder);
        return binder;
    }

    /**
     * Prepare the given binder, applying the specified MessageCodesResolver,
     * BindingErrorProcessor and PropertyEditorRegistrars (if any).
     * Called by {@link #createBinder}.
     * @param binder the new binder instance
     * @see #createBinder
     * @see #setMessageCodesResolver
     * @see #setBindingErrorProcessor
     */
    protected final void prepareBinder(ServletRequestDataBinder binder) {
        if (useDirectFieldAccess()) {
            binder.initDirectFieldAccess();
        }
        if (this.messageCodesResolver != null) {
            binder.setMessageCodesResolver(this.messageCodesResolver);
        }
        if (this.bindingErrorProcessor != null) {
            binder.setBindingErrorProcessor(this.bindingErrorProcessor);
        }
        if (this.propertyEditorRegistrars != null) {
            for (int i = 0; i < this.propertyEditorRegistrars.length; i++) {
                this.propertyEditorRegistrars[i].registerCustomEditors(binder);
            }
        }
    }

    /**
     * Determine whether to use direct field access instead of bean property access.
     * Applied by {@link #prepareBinder}.
     * <p>Default is "false". Can be overridden in subclasses.
     * @see #prepareBinder
     * @see org.springframework.validation.DataBinder#initDirectFieldAccess()
     */
    protected boolean useDirectFieldAccess() {
        return false;
    }

    /**
     * Initialize the given binder instance, for example with custom editors.
     * Called by {@link #createBinder}.
     * <p>This method allows you to register custom editors for certain fields of your
     * command class. For instance, you will be able to transform Date objects into a
     * String pattern and back, in order to allow your JavaBeans to have Date properties
     * and still be able to set and display them in an HTML interface.
     * <p>The default implementation is empty.
     * @param request current HTTP request
     * @param binder the new binder instance
     * @throws Exception in case of invalid state or arguments
     * @see #createBinder
     * @see org.springframework.validation.DataBinder#registerCustomEditor
     * @see org.springframework.beans.propertyeditors.CustomDateEditor
     */
    protected void initBinder(HttpServletRequest request,
            ServletRequestDataBinder binder) throws Exception {
    }

    /**
     * Callback for custom post-processing in terms of binding.
     * Called on each submit, after standard binding but before validation.
     * <p>The default implementation delegates to {@link #onBind(HttpServletRequest, Object)}.
     * @param request current HTTP request
     * @param command the command object to perform further binding on
     * @param errors validation errors holder, allowing for additional
     * custom registration of binding errors
     * @throws Exception in case of invalid state or arguments
     * @see #bindAndValidate
     * @see #onBind(HttpServletRequest, Object)
     */
    protected void onBind(HttpServletRequest request, Object command,
            BindException errors) throws Exception {

        onBind(request, command);
    }

    /**
     * Callback for custom post-processing in terms of binding.
     * <p>Called by the default implementation of the
     * {@link #onBind(HttpServletRequest, Object, BindException)} variant
     * with all parameters, after standard binding but before validation.
     * <p>The default implementation is empty.
     * @param request current HTTP request
     * @param command the command object to perform further binding on
     * @throws Exception in case of invalid state or arguments
     * @see #onBind(HttpServletRequest, Object, BindException)
     */
    protected void onBind(HttpServletRequest request, Object command)
            throws Exception {
    }

    /**
     * Return whether to suppress validation for the given request.
     * <p>The default implementation delegates to {@link #suppressValidation(HttpServletRequest)}.
     * @param request current HTTP request
     * @param command the command object to validate
     * @return whether to suppress validation for the given request
     */
    protected boolean suppressValidation(HttpServletRequest request,
            Object command) {
        return suppressValidation(request);
    }

    /**
     * Return whether to suppress validation for the given request.
     * <p>Called by the default implementation of the
     * {@link #suppressValidation(HttpServletRequest, Object)} variant
     * with all parameters.
     * <p>The default implementation is empty.
     * @param request current HTTP request
     * @return whether to suppress validation for the given request
     */
    protected boolean suppressValidation(HttpServletRequest request) {
        return false;
    }

    /**
     * Callback for custom post-processing in terms of binding and validation.
     * Called on each submit, after standard binding and validation,
     * but before error evaluation.
     * <p>The default implementation is empty.
     * @param request current HTTP request
     * @param command the command object, still allowing for further binding
     * @param errors validation errors holder, allowing for additional
     * custom validation
     * @throws Exception in case of invalid state or arguments
     * @see #bindAndValidate
     * @see org.springframework.validation.Errors
     */
    protected void onBindAndValidate(HttpServletRequest request,
            Object command, BindException errors) throws Exception {
    }
}
