package androidtool.popup.actions;

import java.io.File;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jdt.internal.core.JarPackageFragmentRoot;
import org.eclipse.jdt.internal.core.PackageFragment;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;

import androidtool.Activator;
import androidtool.popup.handler.IActionHandler;
import androidtool.popup.handler.IPlugLogger;
import androidtool.popup.handler.XML2JFileHandler;

/**
 * @author zju_wjf
 * 
 */
@SuppressWarnings("restriction")
public class NewAction implements IObjectActionDelegate, IPlugLogger {
	private Object selected = null;

	@SuppressWarnings("unused")
	private Class<?> selectedClass = null;

	private final IActionHandler handler;

	public NewAction() {
		super();
		handler = new XML2JFileHandler(this);
	}

	public String getSelectPath() {
		if ("unknown".equals(this.selected)) {
			return null;
		}
		File directory = null;
		if ((this.selected instanceof IResource))
			directory = new File(((IResource) this.selected).getLocation().toOSString());
		else if ((this.selected instanceof File)) {
			directory = (File) this.selected;
		}

		if (directory != null) {
			return directory.toString();
		}

		return null;
	}

	public void selectionChanged(IAction action, ISelection selection) {
		try {
			IAdaptable adaptable = null;
			this.selected = "unknown";
			if ((selection instanceof IStructuredSelection)) {
				adaptable = (IAdaptable) ((IStructuredSelection) selection).getFirstElement();
				this.selectedClass = adaptable.getClass();
				if ((adaptable instanceof IResource))
					this.selected = ((IResource) adaptable);
				else if (((adaptable instanceof PackageFragment)) && ((((PackageFragment) adaptable).getPackageFragmentRoot() instanceof JarPackageFragmentRoot)))
					this.selected = getJarFile(((PackageFragment) adaptable).getPackageFragmentRoot());
				else if ((adaptable instanceof JarPackageFragmentRoot))
					this.selected = getJarFile(adaptable);
				else
					this.selected = ((IResource) adaptable.getAdapter(IResource.class));
			}
		} catch (Throwable e) {
		}
	}

	private File getJarFile(IAdaptable adaptable) {
		JarPackageFragmentRoot jpfr = (JarPackageFragmentRoot) adaptable;
		File selected = jpfr.getPath().makeAbsolute().toFile();
		if (!selected.exists()) {
			File projectFile = new File(jpfr.getJavaProject().getProject().getLocation().toOSString());
			selected = new File(projectFile.getParent() + selected.toString());
		}
		return selected;
	}

	@Override
	public void run(IAction action) {
		final String projectPath = Activator.getCurrentProjectDir();
		final String filePath = getSelectPath();

		// if(handler == null) {
		// handler = new XML2JFileHandler(this);
		// }

		handler.handle(projectPath, filePath);
	}

	@Override
	public void log(String log) {
		MessageDialog.openInformation(new Shell(), "XML2Java", log);
	}

	public void dispose() {
	}

	public void init(IWorkbenchWindow arg0) {
	}

	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
	}
}
