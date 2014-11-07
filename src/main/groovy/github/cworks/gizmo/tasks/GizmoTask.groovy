/**
 * Created with love by corbett.
 * User: corbett
 * Date: 11/7/14
 * Time: 1:53 PM
 */
package github.cworks.gizmo.tasks

import github.cworks.gizmo.Gizmo

abstract class GizmoTask {

    protected Gizmo gizmo;

    GizmoTask(final Gizmo gizmo) {
        this.gizmo = gizmo;
    }

    /**
     * A technical term for doing something with great gizziness
     */
    abstract void gizIt();
}
