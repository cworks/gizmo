package github.cworks.gizmo.tasks

import github.cworks.gizmo.GizmoApp
import github.cworks.gizmo.Terminal;

class NewJavaClass extends GizmoTask {

    /**
     * Creates a new Java class by default in src/main/java if it exists, otherwise
     * it prompts to create src/main/java, if user selects 'Y' then new class is
     * created in src/main/java, if 'N' then new class is created from current directory.
     */
    NewJavaClass(GizmoApp gizmo) {
        super(gizmo)
    }

    @Override
    void gizIt() {
        Terminal.println("NewJavaClass, gizIt called");
        //File srcDir = new File("src/main/java");
        //if(!srcDir.exists()) {
            
        //}
    }

}
