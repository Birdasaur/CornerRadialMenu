package radial.cornerradialmenu;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ParallelTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.effect.Shadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.stage.Stage;
import javafx.util.Duration;


/**
 * @author Birdasaur
 * @adapted From Mr. LoNee's awesome RadialMenu example. Source for original 
 * prototype can be found in JFXtras-labs project.
 * https://github.com/JFXtras/jfxtras-labs
 */
public class App extends Application {
    protected Label actionPerformedLabel = new Label();
    protected boolean show;
    protected double lastOffsetValue;
    protected double lastInitialAngleValue;
    private FadeTransition textFadeTransition;
    private RadialMenu radialMenu;
    double ITEM_SIZE = 75.0;
    double ITEM_FIT_WIDTH = 50.0;
    double MENU_SIZE = 250.0;
    double OFFSET = 30.0;
    double INITIAL_ANGLE = -90.0;
    double STROKE_WIDTH = 1.75;
            
    Color bgLg1Color = Color.DARKCYAN.deriveColor(1, 1, 1, 0.2);
    Color bgLg2Color = Color.LIGHTBLUE.deriveColor(1, 1, 1, 0.5);
    Color bgMoLg1Color = Color.LIGHTSKYBLUE.deriveColor(1, 1, 1, 0.3);
    Color bgMoLg2Color = Color.DARKBLUE.deriveColor(1, 1, 1, 0.6);    
    Color strokeColor = Color.ALICEBLUE;
    Color strokeMouseOnColor = Color.YELLOW;
    Color outlineColor = Color.GREEN;    
    Color outlineMouseOnColor = Color.LIGHTGREEN;
    
    private ColorPicker bglg1ColorPicker;
    private ColorPicker bglg2ColorPicker;
    private ColorPicker bgMolg1ColorPicker;
    private ColorPicker bgMolg2ColorPicker;
    private ColorPicker strokeColorPicker;
    private ColorPicker strokeMoColorPicker;
    private ColorPicker outlineColorPicker;
    private ColorPicker outlineMoColorPicker;
    
    private Slider itemSizeSlider;
    private Slider innerRadiusSlider;
    private Slider itemFitWidthSlider;
    private Slider menuSizeSlider;
    private Slider offsetSlider;
    private Slider initialAngleSlider;    
    private Slider strokeWidthSlider;
    
    @Override
    public void start(Stage stage) {
        radialMenu = createCenterRadialMenu();
        radialMenu.setTranslateX(500);
        radialMenu.setTranslateY(500);        
        Pane pane = new Pane(radialMenu);
        HBox colors = createColorControls();
        VBox controls = createSliderControls();
        BorderPane bp = new BorderPane(pane);
        ScrollPane sp = new ScrollPane(controls);
        sp.setFitToWidth(true);
        sp.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        bp.setBottom(sp);
        bp.setTop(colors);
        //make transparent so it doesn't interfere with subnode transparency effects
        Background transBack = new Background(new BackgroundFill(
                Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY));
        pane.setBackground(transBack);
        bp.setBackground(transBack);
        sp.setBackground(transBack);
        var scene = new Scene(bp, 1500, 1000, Color.BLACK);
        String CSS = this.getClass().getResource("/radial/cornerradialmenu/css/styles.css").toExternalForm();
        scene.getStylesheets().add(CSS);     
        
        radialMenu.hideRadialMenu();
        stage.setScene(scene);
        stage.show();
    }
    
    private HBox createColorControls() {
        HBox hbox = new HBox(10);
        hbox.setAlignment(Pos.CENTER);
        bglg1ColorPicker = new ColorPicker(bgLg1Color);
        bglg1ColorPicker.valueProperty().addListener((ov, t, t1) -> {
            radialMenu.setBackgroundFill(new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, bglg1ColorPicker.getValue()), new Stop(0.8, bglg2ColorPicker.getValue())));
        });
        bglg2ColorPicker = new ColorPicker(bgLg2Color);
        bglg2ColorPicker.valueProperty().addListener((ov, t, t1) -> {
            radialMenu.setBackgroundFill(new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, bglg1ColorPicker.getValue()), new Stop(0.8, bglg2ColorPicker.getValue())));
        });

        bgMolg1ColorPicker = new ColorPicker(bgMoLg1Color);
        bgMolg1ColorPicker.valueProperty().addListener((ov, t, t1) -> {
            radialMenu.setBackgroundMouseOnFill(new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, bgMolg1ColorPicker.getValue()), new Stop(0.8, bgMolg2ColorPicker.getValue())));
        });
        
        bgMolg2ColorPicker = new ColorPicker(bgMoLg2Color);     
        bgMolg2ColorPicker.valueProperty().addListener((ov, t, t1) -> {
            radialMenu.setBackgroundMouseOnFill(new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, bgMolg1ColorPicker.getValue()), new Stop(0.8, bgMolg2ColorPicker.getValue())));
        });        

        strokeColorPicker = new ColorPicker(bgMoLg1Color);
        bgMolg1ColorPicker.valueProperty().addListener((ov, t, t1) -> {
            radialMenu.setBackgroundMouseOnFill(new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, bgMolg1ColorPicker.getValue()), new Stop(0.8, bgMolg2ColorPicker.getValue())));
        });
        
        outlineColorPicker = new ColorPicker(bgMoLg2Color);     
        bgMolg2ColorPicker.valueProperty().addListener((ov, t, t1) -> {
            radialMenu.setBackgroundMouseOnFill(new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, bgMolg1ColorPicker.getValue()), new Stop(0.8, bgMolg2ColorPicker.getValue())));
        });        

        strokeColorPicker = new ColorPicker(strokeColor);
        strokeColorPicker.valueProperty().addListener((ov, t, t1) -> {
            radialMenu.setStrokeColor(t1);
      });
        
        strokeMoColorPicker = new ColorPicker(strokeMouseOnColor);     
        strokeMoColorPicker.valueProperty().addListener((ov, t, t1) -> {
            radialMenu.setStrokeMouseOnColor(t1);
      });        

        outlineColorPicker = new ColorPicker(outlineColor);
        outlineColorPicker.valueProperty().addListener((ov, t, t1) -> {
            radialMenu.setOutlineStrokeFill(t1);
      });
        
        outlineMoColorPicker = new ColorPicker(outlineMouseOnColor);     
        outlineMoColorPicker.valueProperty().addListener((ov, t, t1) -> {
            radialMenu.setOutlineStrokeMouseOnFill(t1);
       });        
        
        hbox.getChildren().addAll(bglg1ColorPicker, bglg2ColorPicker, bgMolg1ColorPicker, bgMolg2ColorPicker, 
                    strokeColorPicker, strokeMoColorPicker, outlineColorPicker, outlineMoColorPicker);
        return hbox;
    }

    private VBox createSliderControls() {
        VBox vbox = new VBox(10);
        itemSizeSlider = new Slider(10, 200, ITEM_SIZE);
        itemSizeSlider.valueProperty().addListener((ov, t, t1) -> {
            radialMenu.items.stream().forEach(item -> 
                item.setMenuSize(ov.getValue().doubleValue()));
        });
        innerRadiusSlider = new Slider(10, 200, ITEM_SIZE);
        innerRadiusSlider.valueProperty().addListener((ov, t, t1) -> 
            radialMenu.setInnerRadius(ov.getValue().doubleValue()));
        
        itemFitWidthSlider = new Slider(10, 200, ITEM_FIT_WIDTH);
        itemFitWidthSlider.valueProperty().addListener((ov, t, t1) -> {
            Node centerNode = radialMenu.getCenterGraphic();
            if(centerNode instanceof ImageView) {
                ImageView civ = (ImageView)centerNode;
                civ.setFitWidth(ov.getValue().doubleValue());
                civ.setTranslateX(-ov.getValue().doubleValue() / 2.0);
                civ.setTranslateY(-ov.getValue().doubleValue() / 2.0);
            }
            radialMenu.items.stream().forEach(item -> {
                Node node = item.getGraphic();
                if(node instanceof ImageView) {
                    ImageView iv = (ImageView)node;
                    iv.setFitWidth(ov.getValue().doubleValue());
                }
            });
        });

        menuSizeSlider = new Slider(10, 1000, MENU_SIZE);
        menuSizeSlider.valueProperty().addListener((ov, t, t1) -> 
            radialMenu.setRadius(ov.getValue().doubleValue()));

        offsetSlider = new Slider(1, 200, OFFSET);
        offsetSlider.valueProperty().addListener((ov, t, t1) -> 
            radialMenu.setOffset(ov.getValue().doubleValue()));

        initialAngleSlider = new Slider(-359, 359, INITIAL_ANGLE); 
        initialAngleSlider.valueProperty().addListener((ov, t, t1) -> 
            radialMenu.setInitialAngle(ov.getValue().doubleValue()));
        
        strokeWidthSlider = new Slider(0, 10, STROKE_WIDTH);
        strokeWidthSlider.valueProperty().addListener((ov, t, t1) -> 
            radialMenu.setStrokeWidth(ov.getValue().doubleValue()));
        
        vbox.getChildren().addAll(itemSizeSlider, innerRadiusSlider, itemFitWidthSlider, 
            menuSizeSlider, offsetSlider, initialAngleSlider, strokeWidthSlider);
        vbox.setFillWidth(true);
        return vbox;
    }
    
    private void hideRadialMenu() {
        FadeTransition fade = new FadeTransition(Duration.millis(300), radialMenu);
        fade.setFromValue(1);
        fade.setToValue(0);
        fade.setOnFinished(e->radialMenu.setVisible(false));
        
        final ParallelTransition transition = new ParallelTransition(fade);
        transition.play();
    }

    private void showRadialMenu(final double x, final double y) {
        if (this.radialMenu.isVisible()) {
            this.lastInitialAngleValue = this.radialMenu.getInitialAngle();
            this.lastOffsetValue = this.radialMenu.getOffset();
            this.radialMenu.setVisible(false);
        }
        this.radialMenu.setTranslateX(x);
        this.radialMenu.setTranslateY(y);
        this.radialMenu.setVisible(true);

        FadeTransition fade = new FadeTransition(Duration.millis(400), radialMenu);
        fade.setFromValue(0);
        fade.setToValue(1);
  
        final Animation offset = new Timeline(new KeyFrame(Duration.ZERO,
                new KeyValue(this.radialMenu.offsetProperty(), 0)),
                new KeyFrame(Duration.millis(300), new KeyValue(this.radialMenu
                                .offsetProperty(), this.lastOffsetValue)));

        final Animation angle = new Timeline(new KeyFrame(Duration.ZERO,
                new KeyValue(this.radialMenu.initialAngleProperty(),
                        this.lastInitialAngleValue + 20)), new KeyFrame(
                        Duration.millis(300), new KeyValue(
                                this.radialMenu.initialAngleProperty(),
                                this.lastInitialAngleValue)));

        final ParallelTransition transition = new ParallelTransition(fade, offset, angle);
        transition.play();    
    }

    public RadialMenu createCenterRadialMenu() {
        LinearGradient background = new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
            new Stop(0, bgLg1Color), new Stop(0.8, bgLg2Color));
        LinearGradient backgroundMouseOn = new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
            new Stop(0, bgMoLg1Color), new Stop(0.8, bgMoLg2Color));     
        
        ImageView iv = new ImageView(new Image(this.getClass().getResourceAsStream("head.png")));
        iv.setPreserveRatio(true);
        iv.setFitWidth(ITEM_FIT_WIDTH);
        iv.setTranslateX(-ITEM_FIT_WIDTH/2.0);
        iv.setTranslateY(-ITEM_FIT_WIDTH/2.0);
        
        radialMenu = new RadialMenu(INITIAL_ANGLE, ITEM_SIZE, MENU_SIZE, OFFSET, 
            background, backgroundMouseOn, strokeColor, strokeMouseOnColor, 
            false, RadialMenu.CenterVisibility.ALWAYS, iv);
        radialMenu.setStrokeWidth(STROKE_WIDTH);
        radialMenu.setOutlineStrokeWidth(STROKE_WIDTH);
        radialMenu.setOutlineStrokeFill(outlineColor);
        radialMenu.setOutlineStrokeMouseOnFill(outlineMouseOnColor);
        Glow glow = new Glow(5.2);
////        radialMenu.setEffect(glow);
//        Bloom bloom = new Bloom(10.75);
//        glow.setInput(bloom);
////        radialMenu.setEffect(bloom);
//        DropShadow ds = new DropShadow(5, Color.DARKSLATEBLUE);
//        radialMenu.setEffect(ds);
        Shadow shadow = new Shadow(BlurType.GAUSSIAN, Color.ALICEBLUE, 50);
        radialMenu.setOutlineEffect(shadow);

        ImageView operatorView = new ImageView(new Image(
            this.getClass().getResourceAsStream("operatorview.png")));
        operatorView.setPreserveRatio(true);
        operatorView.setFitWidth(ITEM_FIT_WIDTH);   
        operatorView.setEffect(glow);

        ImageView metrics = new ImageView(new Image(
            this.getClass().getResourceAsStream("metrics.png")));
        metrics.setPreserveRatio(true);
        metrics.setFitWidth(ITEM_FIT_WIDTH);        
        metrics.setEffect(glow);
        
        ImageView configuration = new ImageView(new Image(
            this.getClass().getResourceAsStream("configuration.png")));
        configuration.setPreserveRatio(true);
        configuration.setFitWidth(ITEM_FIT_WIDTH);        
        configuration.setEffect(glow);

        ImageView scenariogenerator = new ImageView(new Image(
            this.getClass().getResourceAsStream("scenariogenerator.png")));
        scenariogenerator.setPreserveRatio(true);
        scenariogenerator.setFitWidth(ITEM_FIT_WIDTH);        
        scenariogenerator.setEffect(glow);

        final EventHandler<ActionEvent> handler = new EventHandler<>() {
            @Override
            public synchronized void handle(final ActionEvent paramT) {
                final RadialMenuItem item = (RadialMenuItem) paramT.getSource();
                if (textFadeTransition != null
                        && textFadeTransition.getStatus() != Animation.Status.STOPPED) {
                    textFadeTransition.stop();
                    actionPerformedLabel.setOpacity(1.0);
                }
                actionPerformedLabel.setText(item.getText());
                actionPerformedLabel.setVisible(true);
                
                FadeTransition textFadeTransition = new FadeTransition(Duration.millis(400), actionPerformedLabel);
                textFadeTransition.setDelay(Duration.seconds(1));
                textFadeTransition.setFromValue(1);
                textFadeTransition.setToValue(0);                
                textFadeTransition.setOnFinished(e -> {
                    actionPerformedLabel.setVisible(false);
                    actionPerformedLabel.setOpacity(1.0);
                });
                textFadeTransition.play();
            }
        };

        radialMenu.addMenuItem(new RadialMenuItem(ITEM_SIZE, "Operator View", operatorView, handler));
        radialMenu.addMenuItem(new RadialMenuItem(ITEM_SIZE, "Configuration", configuration, handler));
        radialMenu.addMenuItem(new RadialMenuItem(ITEM_SIZE, "Metrics", metrics, handler));
        radialMenu.addMenuItem(new RadialMenuItem(ITEM_SIZE, "Scenario Generator", scenariogenerator, handler));
/* Example from original prototype for adding submenus
        final RadialContainerMenuItem forwardItem = new RadialContainerMenuItem(50, "forward", forward);
        forwardItem.addMenuItem(new RadialMenuItem(30, "forward 5'", fiveSec, handler));
        forwardItem.addMenuItem(new RadialMenuItem(30, "forward 10'", tenSec, handler));
        forwardItem.addMenuItem(new RadialMenuItem(30, "forward 20'", twentySec, handler));
        this.radialMenu.addMenuItem(forwardItem);        
  */      
        return this.radialMenu;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
