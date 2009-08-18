package net.java.nboglpack.joglproject;

import java.awt.Component;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.WizardDescriptor;
import org.openide.WizardValidationException;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;

import static org.openide.WizardDescriptor.*;

/**
 * Panel just asking for basic info.
 */
public class ProjectWizardPanel implements Panel<WizardDescriptor>, ValidatingPanel<WizardDescriptor>, FinishablePanel<WizardDescriptor>
{
	
    private WizardDescriptor wizardDescriptor;
    private ProjectPanelVisual component;
    private final Set<ChangeListener> listeners = new HashSet<ChangeListener>(2);

    /** Creates a new instance of templateWizardPanel */
    public ProjectWizardPanel()
    {
    }

    public Component getComponent()
    {
        if (component == null)
        {
            component = new ProjectPanelVisual(this);
            component.setName(NbBundle.getMessage(ProjectWizardPanel.class, "LBL_CreateProjectStep"));
        }
        return component;
    }

    public HelpCtx getHelp()
    {
        return new HelpCtx(ProjectWizardPanel.class);
    }

    public boolean isValid()
    {
        getComponent();
        return component.valid(wizardDescriptor);
    }

    public final void addChangeListener(ChangeListener l)
    {
        synchronized (listeners)
        {
            listeners.add(l);
        }
    }
    public final void removeChangeListener(ChangeListener l)
    {
        synchronized (listeners)
        {
            listeners.remove(l);
        }
    }
    protected final void fireChangeEvent()
    {
        Iterator<ChangeListener> it;
        synchronized (listeners)
        {
            it = new HashSet<ChangeListener>(listeners).iterator();
        }
        ChangeEvent ev = new ChangeEvent(this);
        while (it.hasNext())
        {
            it.next().stateChanged(ev);
        }
    }

    public void readSettings(WizardDescriptor settings)
    {
        wizardDescriptor = settings;
        component.read(wizardDescriptor);
    }

    public void storeSettings(WizardDescriptor settings)
    {
        WizardDescriptor d = settings;
        component.store(d);
    }

    public boolean isFinishPanel()
    {
        return true;
    }

    public void validate() throws WizardValidationException
    {
        getComponent();
        component.validate(wizardDescriptor);
    }
	
}
