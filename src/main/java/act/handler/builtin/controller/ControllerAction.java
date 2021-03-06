package act.handler.builtin.controller;

import act.app.ActionContext;
import act.security.CORS;
import act.security.CSRF;
import org.osgl.mvc.result.Result;

/**
 * Dispatch request to real controller action method
 */
public class ControllerAction extends ActionHandler<ControllerAction> {

    private ActionHandlerInvoker handlerInvoker;

    public ControllerAction(ActionHandlerInvoker invoker) {
        super(-1);
        this.handlerInvoker = invoker;
    }

    @Override
    public Result handle(ActionContext actionContext) throws Exception {
        return handlerInvoker.handle(actionContext);
    }

    @Override
    public CORS.Spec corsSpec() {
        return handlerInvoker.corsSpec();
    }

    public CSRF.Spec csrfSpec() {
        return handlerInvoker.csrfSpec();
    }

    @Override
    public boolean sessionFree() {
        return handlerInvoker.sessionFree();
    }

    @Override
    public boolean express() {
        return handlerInvoker.express();
    }

    @Override
    public void accept(Visitor visitor) {
        handlerInvoker.accept(visitor.invokerVisitor());
    }

    @Override
    protected void releaseResources() {
        handlerInvoker.destroy();
        handlerInvoker = null;
    }
}
