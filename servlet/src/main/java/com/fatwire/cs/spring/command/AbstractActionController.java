package com.fatwire.cs.spring.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.DataBinder;
import org.springframework.web.servlet.mvc.AbstractCommandController;
import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.web.servlet.mvc.AbstractFormController;
import org.springframework.web.servlet.mvc.AbstractWizardFormController;
import org.springframework.web.servlet.mvc.SimpleFormController;

import com.fatwire.cs.spring.command.bind.ActionRequestBinder;


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
 *      <td>actionName</td>
 *      <td>action</td>
 *      <td>the name to use when binding the action
 *          to the request</td>
 *  </tr>
 *  <tr>
 *      <td>actionFactory</td>
 *      <td>null</td>
 *      <td>The factory to use to create the action</td>
 *  </tr>

 *  <tr>
 *      <td>actionRequestBinder</td>
 *      <td><i>null</i></td>
 *      <td>The request binder that binds the request data to the action.</td>
 *  </tr>
 * </table>
 * </p>
 *
 * @author Dolf Dijkstra
 */
public abstract class AbstractActionController extends AbstractController {

    /** Default command name used for binding command objects: "action" */
    public static final String DEFAULT_ACTION_NAME = "action";

    private String actionName = DEFAULT_ACTION_NAME;

    private ActionFactory actionFactory;

    private ActionRequestBinder actionRequestBinder;

    /**
     * Set the name of the command in the model.
     * The command object will be included in the model under this name.
     */
    public final void setActionName(String commandName) {
        this.actionName = commandName;
    }

    /**
     * Return the name of the command in the model.
     */
    public final String getActionName() {
        return this.actionName;
    }

    /**
     * Retrieve a Action object for the given request.
     * <p>The default implementation calls {@link #createCommand}.
     * Subclasses can override this.
     * @param request current HTTP request
     * @return object action to bind onto
     * @throws Exception if the command object could not be obtained
     */
    protected Action getAction(String key) throws Exception {
        return actionFactory.createAction(key);
    }

    /**
     * Bind the parameters of the given request to the given command object.
     * @param request current HTTP request
     * @param command the command to bind onto
     * @return the ServletRequestDataBinder instance for additional custom validation
     * @throws Exception in case of invalid state or arguments
     */
    protected final DataBinder bindAndValidate(HttpServletRequest request,
            HttpServletResponse response, Action action) throws Exception {
        return actionRequestBinder.bindAndValidate(request, action,
                getActionName());
    }

    /**
     * @return the actionFactory
     */
    public ActionFactory getActionFactory() {
        return actionFactory;
    }

    /**
     * @param actionFactory the actionFactory to set
     */
    public void setActionFactory(ActionFactory actionFactory) {
        this.actionFactory = actionFactory;
    }

    /**
     * @return the actionRequestBinder
     */
    public ActionRequestBinder getActionRequestBinder() {
        return actionRequestBinder;
    }

    /**
     * @param actionRequestBinder the actionRequestBinder to set
     */
    public void setActionRequestBinder(ActionRequestBinder actionRequestBinder) {
        this.actionRequestBinder = actionRequestBinder;
    }

}
