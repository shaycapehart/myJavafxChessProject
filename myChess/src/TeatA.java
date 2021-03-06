
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.PickResult;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Shear;
import javafx.scene.transform.Translate;
import javafx.stage.Popup;
import javafx.stage.Stage;

public class TeatA extends Application {
	double mousePosX;
	double mousePosY;
	double mouseOldX;
	double mouseOldY;
	double mouseDeltaX;
	double mouseDeltaY;
	double mouse3dOldX;
	double mouse3dOldZ;
	double mouse3dPosX;
	double mouse3dPosZ;
	double mouse3dDeltaX;
	double mouse3dDeltaZ;

	String[] id_parts;
	Node n;

	final int stepSize = 6;

	final Cam camOffset = new Cam();
	final Cam cam = new Cam();

	final Shear shear = new Shear();

	class Cam extends Group {
		Translate t = new Translate();
		Translate p = new Translate();
		Translate ip = new Translate();
		Rotate rx = new Rotate();
		{
			rx.setAxis(Rotate.X_AXIS);
		}
		Rotate ry = new Rotate();
		{
			ry.setAxis(Rotate.Y_AXIS);
		}
		Rotate rz = new Rotate();
		{
			rz.setAxis(Rotate.Z_AXIS);
		}
		Scale s = new Scale();

		public Cam() {
			super();
			getTransforms().addAll(t, p, rx, rz, ry, s, ip);
		}
	}

	@Override
	public void start(final Stage stage) throws Exception {
		stage.setTitle("3-D Chess");
		camOffset.getChildren().add(cam);
		resetCam();

		// create a label
		Label labelX = new Label("This is for x coord");
		Label labelY = new Label("This is for y coord");
		Label labelZ = new Label("This is for z coord");

		// create a popup
		Popup popupX = new Popup();
		Popup popupY = new Popup();
		Popup popupZ = new Popup();

		// set background
		labelX.setStyle(" -fx-background-color: white;");
		labelY.setStyle(" -fx-background-color: white;");
		labelZ.setStyle(" -fx-background-color: white;");

		// add the label
		popupX.getContent().add(labelX);
		popupY.getContent().add(labelY);
		popupZ.getContent().add(labelZ);

		// set size of label
		labelX.setMinWidth(80);
		labelX.setMinHeight(50);
		popupX.setX(100);
		popupX.setY(100);
		labelY.setMinWidth(80);
		labelY.setMinHeight(50);
		popupY.setX(100);
		popupY.setY(200);
		labelZ.setMinWidth(80);
		labelZ.setMinHeight(50);
		popupZ.setX(100);
		popupZ.setY(300);

		final Scene scene = new Scene(camOffset, 800, 600, true, SceneAntialiasing.BALANCED);
		scene.setFill(new RadialGradient(255, 0.85, 400, 300, 500, false, CycleMethod.NO_CYCLE,
				new Stop[] { new Stop(0f, Color.BLUE), new Stop(1f, Color.LIGHTBLUE) }));
		scene.setCamera(new PerspectiveCamera());

		// ADDING A 3D Model via FXML
		FXMLLoader fxmlLoader = new FXMLLoader();
		fxmlLoader.setLocation(this.getClass().getResource("ChessScene.fxml"));
		Group graphic = fxmlLoader.<Group>load();
		cam.getChildren().add(graphic);

		double halfSceneWidth = scene.getWidth() / 2.0;
		double halfSceneHeight = scene.getHeight() / 2.0;
		cam.p.setX(halfSceneWidth);
		cam.ip.setX(-halfSceneWidth);
		cam.p.setY(halfSceneHeight);
		cam.ip.setY(-halfSceneHeight);

		frameCam(stage, scene);

//		scene.setOnMousePressed(new EventHandler<MouseEvent>() {
//			public void handle(MouseEvent me) {
//				mousePosX = me.getX();
//				mousePosY = me.getY();
//				mouseOldX = me.getX();
//				mouseOldY = me.getY();
//			}
//		});
		scene.setOnMousePressed(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent me) {
				mousePosX = me.getX();
				mousePosY = me.getY();
				mouseOldX = me.getX();
				mouseOldY = me.getY();
				Point3D coords = me.getPickResult().getIntersectedPoint();
				mouse3dPosX = coords.getX();
				mouse3dPosZ = coords.getZ();
				mouse3dOldX = coords.getX();
				mouse3dOldZ = coords.getZ();
				popupX.show(stage);
				popupY.show(stage);
				popupZ.show(stage);
				PickResult pr = me.getPickResult();
				if (pr != null && pr.getIntersectedNode() != null) {
					n = pr.getIntersectedNode();
					id_parts = n.getId().toString().split("_");
					labelX.setText(n.getId().toString());
//					if (!popupX.isShowing())
//						popupX.show(stage);
					if (!"Board".equals(id_parts[0])) {
						if ("Black".equals(id_parts[0])) {
							n.translateXProperty().set(n.getTranslateX() - stepSize);
						} else {
							n.translateXProperty().set(n.getTranslateX() + stepSize);
						}
						switch (id_parts[1]) {
						case "Pawn":
							n.translateXProperty().set(n.getTranslateX() - stepSize);
							break;
						case "Rook":
							n.translateXProperty().set(n.getTranslateX() - stepSize);
							break;
						case "Knight":
							n.translateXProperty().set(n.getTranslateX() - stepSize);
							break;
						case "Bighop":
							n.translateXProperty().set(n.getTranslateX() - stepSize);
							break;
						case "Queen":
							n.translateXProperty().set(n.getTranslateX() - stepSize);
							break;
						case "King":
							n.translateXProperty().set(n.getTranslateX() - stepSize);
							break;
						}
					}
				}
			}
		});
		scene.setOnMouseDragged(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent me) {
				mouseOldX = mousePosX;
				mouseOldY = mousePosY;
				mousePosX = me.getX();
				mousePosY = me.getY();
				mouseDeltaX = mousePosX - mouseOldX;
				mouseDeltaY = mousePosY - mouseOldY;
				if ("Boarder".equals(id_parts[1])) {
					if (me.isAltDown() && me.isShiftDown() && me.isPrimaryButtonDown()) {
						double rzAngle = cam.rz.getAngle();
						cam.rz.setAngle(rzAngle - mouseDeltaX);
					} else if (me.isShiftDown() && me.isPrimaryButtonDown()) {
						double yShear = shear.getY();
						shear.setY(yShear + mouseDeltaY / 1000.0);
						double xShear = shear.getX();
						shear.setX(xShear + mouseDeltaX / 1000.0);
					} else if (me.isAltDown() && me.isSecondaryButtonDown()) {
						double scale = cam.s.getX();
						double newScale = scale + mouseDeltaX * 0.01;
						cam.s.setX(newScale);
						cam.s.setY(newScale);
						cam.s.setZ(newScale);
					} else if (me.isAltDown() && me.isMiddleButtonDown()) {
						double tx = cam.t.getX();
						double ty = cam.t.getY();
						cam.t.setX(tx + mouseDeltaX);
						cam.t.setY(ty + mouseDeltaY);
					} else if (me.isPrimaryButtonDown()) {
						double ryAngle = cam.ry.getAngle();
						cam.ry.setAngle(ryAngle - mouseDeltaX / 10.0);
						double rxAngle = cam.rx.getAngle();
						cam.rx.setAngle(rxAngle + mouseDeltaY / 10.0);
					}
				} else if (!"Board".equals(id_parts[0])) {
					if (me.isPrimaryButtonDown()) {
						double nx = n.getTranslateX();
						double ny = n.getTranslateZ();
						n.setTranslateX(nx + mouseDeltaX / 10.0);
						n.setTranslateZ(ny - mouseDeltaY / 8.0);
//						Point3D coords = me.getPickResult().getIntersectedPoint();
//						mouse3dOldX = mouse3dPosX;
//						mouse3dOldZ = mouse3dPosZ;
//						mouse3dPosX = coords.getX();
//						mouse3dPosZ = coords.getZ();
//						mouse3dDeltaX = mouse3dPosX - mouse3dOldX;
//						mouse3dDeltaZ = mouse3dPosZ - mouse3dOldZ;
//						labelX.setText(Double.toString(mouse3dPosX));
//						labelY.setText(Double.toString(mouse3dPosZ));
//						labelZ.setText(Double.toString(mouse3dDeltaX));
//						n.setTranslateX(n.getTranslateX() + mouse3dDeltaX);
//						n.setTranslateZ(n.getTranslateZ() + mouse3dDeltaZ);
					}
				}
			}
		});
		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent ke) {
				if (KeyCode.A.equals(ke.getCode())) {
					resetCam();
					shear.setX(0.0);
					shear.setY(0.0);
				}
				if (KeyCode.F.equals(ke.getCode())) {
					frameCam(stage, scene);
					shear.setX(0.0);
					shear.setY(0.0);
				}
				if (KeyCode.SPACE.equals(ke.getCode())) {
					if (stage.isFullScreen()) {
						stage.setFullScreen(false);
						frameCam(stage, scene);
					} else {
						stage.setFullScreen(true);
						frameCam(stage, scene);
					}
				}
			}
		});

		stage.setScene(scene);
		stage.show();
	}

	// =========================================================================
	// CubeSystem.frameCam
	// =========================================================================
	public void frameCam(final Stage stage, final Scene scene) {
		setCamOffset(camOffset, scene);
		setCamPivot(cam);
		setCamTranslate(cam);
		setCamScale(cam, scene);
	}

	// =========================================================================
	// CubeSystem.setCamOffset
	// =========================================================================
	public void setCamOffset(final Cam camOffset, final Scene scene) {
		double width = scene.getWidth();
		double height = scene.getHeight();
		camOffset.t.setX(width / 2.0);
		camOffset.t.setY(height / 2.0);
	}

	// =========================================================================
	// setCamScale
	// =========================================================================
	public void setCamScale(final Cam cam, final Scene scene) {
		final Bounds bounds = cam.getBoundsInLocal();

		double width = scene.getWidth();
		double height = scene.getHeight();

		double scaleFactor = 1.0;
//        double scaleFactorY = 1.0;
//        double scaleFactorX = 1.0;
//        if (bounds.getWidth() > 0.0001) {
//            scaleFactorX = width / bounds.getWidth(); // / 2.0;
//        }
//        if (bounds.getHeight() > 0.0001) {
//            scaleFactorY = height / bounds.getHeight(); //  / 1.5;
//        }
//        if (scaleFactorX > scaleFactorY) {
//            scaleFactor = scaleFactorY;
//        } else {
//            scaleFactor = scaleFactorX;
//        }
		scaleFactor = 10.0;
		cam.s.setX(scaleFactor);
		cam.s.setY(scaleFactor);
		cam.s.setZ(scaleFactor);
	}

	// =========================================================================
	// setCamPivot
	// =========================================================================
	public void setCamPivot(final Cam cam) {
		final Bounds bounds = cam.getBoundsInLocal();
		final double pivotX = bounds.getMinX() + bounds.getWidth() / 2;
		final double pivotY = bounds.getMinY() + bounds.getHeight() / 2;
		final double pivotZ = bounds.getMinZ() + bounds.getDepth() / 2;
		cam.p.setX(pivotX);
		cam.p.setY(pivotY);
		cam.p.setZ(pivotZ);
		cam.ip.setX(-pivotX);
		cam.ip.setY(-pivotY);
		cam.ip.setZ(-pivotZ);
	}

	// =========================================================================
	// setCamTranslate
	// =========================================================================
	public void setCamTranslate(final Cam cam) {
		final Bounds bounds = cam.getBoundsInLocal();
		final double pivotX = bounds.getMinX() + bounds.getWidth() / 2;
		final double pivotY = bounds.getMinY() + bounds.getHeight() / 2;
		cam.t.setX(-pivotX);
		cam.t.setY(-pivotY);
	}

	public void resetCam() {
		cam.t.setX(0.0);
		cam.t.setY(0.0);
		cam.t.setZ(0.0);
		cam.rx.setAngle(30.0);
		cam.ry.setAngle(0.0);
		cam.rz.setAngle(0.0);
		cam.s.setX(1.5);
		cam.s.setY(1.5);
		cam.s.setZ(1.5);

		cam.p.setX(0.0);
		cam.p.setY(0.0);
		cam.p.setZ(0.0);

		cam.ip.setX(0.0);
		cam.ip.setY(0.0);
		cam.ip.setZ(0.0);

		final Bounds bounds = cam.getBoundsInLocal();
		final double pivotX = bounds.getMinX() + bounds.getWidth() / 2;
		final double pivotY = bounds.getMinY() + bounds.getHeight() / 2;
		final double pivotZ = bounds.getMinZ() + bounds.getDepth() / 2;

		cam.p.setX(pivotX);
		cam.p.setY(pivotY);
		cam.p.setZ(pivotZ);

		cam.ip.setX(-pivotX);
		cam.ip.setY(-pivotY);
		cam.ip.setZ(-pivotZ);
	}

	public static void main(String[] args) {
		launch(args);
	}
}
