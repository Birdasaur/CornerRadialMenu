/**
 * RadialMenu.java
 * <p>
 * Copyright (c) 2011-2015, JFXtras
 * All rights reserved.
 * <p>
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * * Neither the name of the organization nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 * <p>
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
/**
 * @author Birdasaur
 * @adapted From Mr. LoNee's awesome RadialMenu example. Source for original 
 * prototype can be found in JFXtras-labs project.
 * https://github.com/JFXtras/jfxtras-labs
 */
package radial.cornerradialmenu;

import java.util.ArrayList;
import java.util.List;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ParallelTransition;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.effect.Effect;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

public class RadialMenu extends Group implements EventHandler<MouseEvent>,
        ChangeListener<Object> {

    public enum CenterVisibility {
        ALWAYS, WITH_MENU, NEVER
    }
    public static double DEFAULT_STROKE_WIDTH = 1.0;
    protected List<RadialMenuItem> items = new ArrayList<RadialMenuItem>();
    protected DoubleProperty innerRadius;
    protected DoubleProperty radius;
    protected DoubleProperty offset;
    protected DoubleProperty initialAngle;
    protected DoubleProperty itemFitWidth;
    protected DoubleProperty menuItemSize;
    protected ObjectProperty<Paint> backgroundFill;
    protected ObjectProperty<Paint> backgroundMouseOnFill;

    protected ObjectProperty<Paint> strokeMouseOnFill;
    protected ObjectProperty<Paint> strokeFill;
    protected DoubleProperty strokeWidth;
    protected BooleanProperty strokeVisible;

    protected ObjectProperty<Paint> outlineStrokeMouseOnFill;
    protected ObjectProperty<Paint> outlineStrokeFill;
    protected DoubleProperty outlineStrokeWidth;
    protected BooleanProperty outlineStrokeVisible;
    protected ObjectProperty<Effect> outlineEffect;
    
    protected BooleanProperty clockwise;
    protected BooleanProperty backgroundVisible;
    protected ObjectProperty<CenterVisibility> centerVisibility;
    protected ObjectProperty<Node> centerGraphic;
    protected Circle centerStrokeShape;
    protected Group centerGroup;
    protected Group itemGroup;
    private boolean mouseOn = false;
    protected BooleanProperty mouseOnProperty = new SimpleBooleanProperty(mouseOn);
    private double lastInitialAngleValue;
    private double lastOffsetValue;
    private boolean allowRedraw = true;
    
    
    public Paint getBackgroundFill() {
        return this.backgroundFill.get();
    }

    public void setBackgroundFill(final Paint backgroundFill) {
        this.backgroundFill.set(backgroundFill);
    }

    public ObjectProperty<Paint> backgroundFillProperty() {
        return this.backgroundFill;
    }

    public Paint getBackgroundMouseOnFill() {
        return this.backgroundMouseOnFill.get();
    }

    public void setBackgroundMouseOnFill(final Paint backgroundMouseOnFill) {
        this.backgroundMouseOnFill.set(backgroundMouseOnFill);
    }

    public ObjectProperty<Paint> backgroundMouseOnFillProperty() {
        return this.backgroundMouseOnFill;
    }

    public Paint getStrokeMouseOnFill() {
        return this.strokeMouseOnFill.get();
    }

    public void setStrokeMouseOnFill(final Paint backgroundMouseOnFill) {
        this.strokeMouseOnFill.set(backgroundMouseOnFill);
    }

    public ObjectProperty<Paint> strokeMouseOnFillProperty() {
        return this.strokeMouseOnFill;
    }

    public Paint getStrokeFill() {
        return this.strokeFill.get();
    }

    public void setStrokeFill(final Paint strokeFill) {
        this.strokeFill.set(strokeFill);
    }

    public ObjectProperty<Paint> strokeFillProperty() {
        return this.strokeFill;
    }

    public double getStrokeWidth() {
        return this.strokeWidth.get();
    }

    public void setStrokeWidth(final double width) {
        this.strokeWidth.set(width);
    }

    public DoubleProperty strokeWidthProperty() {
        return this.strokeWidth;
    }

    public BooleanProperty strokeVisibleProperty() {
        return this.strokeVisible;
    }

    public boolean isStrokeVisible() {
        return this.strokeVisible.get();
    }


    public Paint getOutlineStrokeMouseOnFill() {
        return this.outlineStrokeMouseOnFill.get();
    }

    public void setOutlineStrokeMouseOnFill(final Paint backgroundMouseOnFill) {
        this.outlineStrokeMouseOnFill.set(backgroundMouseOnFill);
    }

    public ObjectProperty<Paint> outlineStrokeMouseOnFillProperty() {
        return this.outlineStrokeMouseOnFill;
    }

    public Paint getOutlineStrokeFill() {
        return this.outlineStrokeFill.get();
    }

    public void setOutlineStrokeFill(final Paint strokeFill) {
        this.outlineStrokeFill.set(strokeFill);
    }

    public ObjectProperty<Paint> outlineStrokeFillProperty() {
        return this.outlineStrokeFill;
    }

    public double getOutlineStrokeWidth() {
        return this.outlineStrokeWidth.get();
    }

    public void setOutlineStrokeWidth(final double width) {
        this.outlineStrokeWidth.set(width);
    }

    public DoubleProperty outlineStrokeWidthProperty() {
        return this.outlineStrokeWidth;
    }

    public BooleanProperty outlineStrokeVisibleProperty() {
        return this.outlineStrokeVisible;
    }

    public boolean isOutlinStrokeVisible() {
        return this.outlineStrokeVisible.get();
    }

    public Effect getOutlineEffect() {
        return this.outlineEffect.get();
    }

    public void setOutlineEffect(final Effect outlineEffect) {
        this.outlineEffect.set(outlineEffect);
    }

    public ObjectProperty<Effect> outlineEffectProperty() {
        return this.outlineEffect;
    }    
    
    public Node getCenterGraphic() {
        return this.centerGraphic.get();
    }

    public void setCenterGraphic(final Node graphic) {
        if (this.centerGraphic.get() != null) {
            this.centerGroup.getChildren().remove(this.centerGraphic.get());
        }
        if (graphic != null) {
            this.centerGroup.getChildren().add(graphic);
        }
        this.centerGraphic.set(graphic);
    }

    public ObjectProperty<Node> centerGraphicProperty() {
        return this.centerGraphic;
    }

    public double getInitialAngle() {
        return this.initialAngle.get();
    }

    public DoubleProperty initialAngleProperty() {
        return this.initialAngle;
    }
    public double getItemFitWidth() {
        return itemFitWidth.get();
    }

    public DoubleProperty itemFitWidthProperty() {
        return itemFitWidth;
    }
    public double getMenuItemSize() {
        return menuItemSize.get();
    }

    public DoubleProperty menuItemSizeProperty() {
        return menuItemSize;
    }

    public double getInnerRadius() {
        return this.innerRadius.get();
    }

    public DoubleProperty innerRadiusProperty() {
        return this.innerRadius;
    }

    public double getRadius() {
        return this.radius.get();
    }

    public DoubleProperty radiusProperty() {
        return this.radius;
    }

    public double getOffset() {
        return this.offset.get();
    }

    public DoubleProperty offsetProperty() {
        return this.offset;
    }

    public boolean isClockwise() {
        return this.clockwise.get();
    }

    public BooleanProperty clockwiseProperty() {
        return this.clockwise;
    }

    public boolean isBackgroundVisible() {
        return this.backgroundVisible.get();
    }

    public BooleanProperty backgroundVisibleProperty() {
        return this.backgroundVisible;
    }

    public ObjectProperty<CenterVisibility> centerVisibilityProperty() {
        return this.centerVisibility;
    }

    public CenterVisibility getCenterVisibility() {
        return this.centerVisibility.get();
    }

    public void setCenterVisibility(final CenterVisibility visibility) {
        this.centerVisibility.set(visibility);
    }

    public RadialMenu() {
    }

    public RadialMenu(final double initialAngle, final double innerRadius,
                      final double radius, final double offset, final Paint bgFill,
                      final Paint bgMouseOnFill, final Paint strokeFill,
                      final Paint strokeMouseOnFill, final boolean clockwise,
                      final CenterVisibility centerVisibility, final Node centerGraphic) {

        this.itemGroup = new Group();
        this.getChildren().add(this.itemGroup);
        
        itemFitWidth = new SimpleDoubleProperty(innerRadius);
        itemFitWidth.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(
                final ObservableValue<? extends Number> paramObservableValue,
                final Number paramT1, final Number paramT2) {
                    setGraphicsFitWidth(paramObservableValue.getValue().doubleValue());
            }            
        });
        menuItemSize = new SimpleDoubleProperty(innerRadius);
        menuItemSize.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(
                final ObservableValue<? extends Number> paramObservableValue,
                final Number paramT1, final Number paramT2) {
                    setMenuItemSize(paramObservableValue.getValue().doubleValue());
            }            
        });
        
        this.initialAngle = new SimpleDoubleProperty(initialAngle);
        this.initialAngle.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(
                final ObservableValue<? extends Number> paramObservableValue,
                final Number paramT1, final Number paramT2) {
                    setInitialAngle(paramObservableValue.getValue().doubleValue());
            }
        });

        this.innerRadius = new SimpleDoubleProperty(innerRadius);
        this.strokeFill = new SimpleObjectProperty<>(strokeFill);
        this.strokeFill.addListener(this);
        this.strokeWidth = new SimpleDoubleProperty(DEFAULT_STROKE_WIDTH);
        this.strokeWidth.addListener(this);        
        this.strokeMouseOnFill = new SimpleObjectProperty<>(strokeMouseOnFill);
        this.strokeMouseOnFill.addListener(this);
        this.strokeVisible = new SimpleBooleanProperty(true);
        this.strokeVisible.addListener(this);
        
        outlineStrokeFill = new SimpleObjectProperty<>(strokeFill);
        outlineStrokeFill.addListener(this);
        outlineStrokeWidth = new SimpleDoubleProperty(DEFAULT_STROKE_WIDTH);
        outlineStrokeWidth.addListener(this);        
        outlineStrokeMouseOnFill = new SimpleObjectProperty<>(strokeMouseOnFill);
        outlineStrokeMouseOnFill.addListener(this);
        outlineStrokeVisible = new SimpleBooleanProperty(true);
        outlineStrokeVisible.addListener(this);   
        outlineEffect = new SimpleObjectProperty<>(null);
        outlineEffect.addListener(this);
        
        this.radius = new SimpleDoubleProperty(radius);
        this.offset = new SimpleDoubleProperty(offset);
        this.clockwise = new SimpleBooleanProperty(clockwise);
        this.backgroundFill = new SimpleObjectProperty<>(bgFill);
        this.backgroundFill.addListener(this);
        this.backgroundMouseOnFill = new SimpleObjectProperty<>(bgMouseOnFill);
        this.backgroundMouseOnFill.addListener(this);
        this.backgroundVisible = new SimpleBooleanProperty(true);
        this.centerVisibility = new SimpleObjectProperty<>(centerVisibility);
        this.centerStrokeShape = new Circle(innerRadius);
        this.centerStrokeShape.radiusProperty().bind(innerRadiusProperty());
        this.centerStrokeShape.setStroke(strokeFill);
        this.centerStrokeShape.setStrokeWidth(strokeWidth.get());
        this.centerStrokeShape.setFill(bgFill);
        this.centerVisibility.addListener(this);
        this.backgroundVisible.addListener(this);

        this.centerGroup = new Group();
        this.centerGroup.getChildren().add(this.centerStrokeShape);
        this.centerGroup.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(final MouseEvent event) {
                mouseOn = true;
                mouseOnProperty.set(mouseOn);
                redraw();
            }
        });
        this.centerGroup.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(final MouseEvent event) {
                mouseOn = false;
                mouseOnProperty.set(mouseOn);
                redraw();
            }
        });
        this.centerGroup.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(final MouseEvent event) {
                if(!event.isControlDown()) {
                    final boolean visible = itemGroup.isVisible();
                    if (visible) {
                        RadialMenu.this.hideRadialMenu();
                    } else {
                        RadialMenu.this.showRadialMenu();
                    }
                }
                event.consume();
            }
        });


        this.getChildren().add(this.centerGroup);
        this.centerGraphic = new SimpleObjectProperty<Node>(centerGraphic);
        this.setCenterGraphic(centerGraphic);

        this.saveStateBeforeAnimation();
    }
    
    public void setGraphicsFitWidth(double fitWidth) {
        Node centerNode = getCenterGraphic();
        if(centerNode instanceof ImageView) {
            ImageView civ = (ImageView)centerNode;
            civ.setFitWidth(fitWidth);
            civ.setTranslateX(-fitWidth / 2.0);
            civ.setTranslateY(-fitWidth / 2.0);
        }
        items.stream().forEach(item -> {
            Node node = item.getGraphic();
            if(node instanceof ImageView) {
                ImageView iv = (ImageView)node;
                iv.setFitWidth(fitWidth);
            }
        });        
    }
    public void setMenuItemSize(double menuItemSize) {
        items.stream().forEach(item -> item.setMenuSize(menuItemSize));
    }
    public void setOnMenuItemMouseClicked(
            final EventHandler<? super MouseEvent> paramEventHandler) {
        for (final RadialMenuItem item : this.items) {
            item.setOnMouseClicked(paramEventHandler);
        }
    }

    public void setInitialAngle(final double angle) {
        this.initialAngle.set(angle);

        double angleOffset = this.initialAngle.get();
        for (final RadialMenuItem item : this.items) {
            item.setStartAngle(angleOffset);
            angleOffset = angleOffset + item.getMenuSize();
        }
    }

    public void setInnerRadius(final double radius) {
        this.innerRadius.set(radius);

    }

    public void setRadius(final double radius) {
        this.radius.set(radius);

    }

    public void setOffset(final double offset) {
        this.offset.set(offset);
    }

    public void setBackgroundVisible(final boolean visible) {
        this.backgroundVisible.set(visible);

    }

    public void setStrokeVisible(final boolean visible) {
        this.strokeVisible.set(visible);
    }

    public void setBackgroundColor(final Paint color) {
        this.backgroundFill.set(color);
    }

    public void setBackgroundMouseOnColor(final Paint color) {
        this.backgroundMouseOnFill.set(color);
    }

    public void setStrokeMouseOnColor(final Paint color) {
        this.strokeMouseOnFill.set(color);
    }

    public void setStrokeColor(final Paint color) {
        this.strokeFill.set(color);
    }

    public void setClockwise(final boolean clockwise) {
        this.clockwise.set(clockwise);
    }

    public void addMenuItem(final RadialMenuItem item) {
        item.visibleProperty().bind(this.visibleProperty());
        item.backgroundColorProperty().bind(this.backgroundFill);
        item.backgroundMouseOnColorProperty().bind(this.backgroundMouseOnFill);
        item.backgroundVisibleProperty().bind(this.backgroundVisible);

        item.innerRadiusProperty().bind(this.innerRadius);
        item.radiusProperty().bind(this.radius);
        item.offsetProperty().bind(this.offset);
        item.strokeMouseOnColorProperty().bind(this.strokeMouseOnFill);
        item.strokeColorProperty().bind(this.strokeFill);
        item.strokeWidthProperty().bind(this.strokeWidth);
        item.strokeVisibleProperty().bind(this.strokeVisible);
        item.outlineStrokeMouseOnColorProperty().bind(this.outlineStrokeMouseOnFill);
        item.outlineStrokeColorProperty().bind(this.outlineStrokeFill);
        item.outlineStrokeWidthProperty().bind(this.outlineStrokeWidth);
        item.outlineStrokeVisibleProperty().bind(this.outlineStrokeVisible);
        item.outlineEffectProperty().bind(this.outlineEffect);

        item.clockwiseProperty().bind(this.clockwise);
        this.items.add(item);
        this.itemGroup.getChildren().add(this.itemGroup.getChildren().size(), item);
        double angleOffset = this.initialAngle.get();
        for (final RadialMenuItem it : this.items) {
            it.setStartAngle(angleOffset);
            angleOffset = angleOffset + item.getMenuSize();
        }
        item.setOnMouseClicked(this);
    }

    public void removeMenuItem(final RadialMenuItem item) {
        this.items.remove(item);
        this.itemGroup.getChildren().remove(item);
        item.visibleProperty().unbind();
        item.backgroundColorProperty().unbind();
        item.backgroundMouseOnColorProperty().unbind();
        item.innerRadiusProperty().unbind();
        item.radiusProperty().unbind();
        item.offsetProperty().unbind();
        item.clockwiseProperty().unbind();
        item.backgroundVisibleProperty().unbind();
        item.strokeMouseOnColorProperty().unbind();
        item.strokeColorProperty().unbind();
        item.strokeWidthProperty().unbind();
        item.strokeVisibleProperty().unbind();
        item.outlineStrokeMouseOnColorProperty().unbind();
        item.outlineStrokeColorProperty().unbind();
        item.outlineStrokeWidthProperty().unbind();
        item.outlineStrokeVisibleProperty().unbind();
        item.outlineEffectProperty().unbind();

        item.removeEventHandler(MouseEvent.MOUSE_CLICKED, this);
    }

    public void removeMenuItem(final int itemIndex) {
        final RadialMenuItem item = this.items.get(itemIndex);
        this.removeMenuItem(item);
    }

    @Override
    public void handle(final MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
            final RadialMenuItem item = (RadialMenuItem) event.getSource();
            item.setSelected(!item.isSelected());
            for (final RadialMenuItem it : this.items) {
                if (it != item) {
                    it.setSelected(false);
                }
            }
            if (!item.isSelected()) {
                this.hideRadialMenu();
            }
            event.consume();
        }
    }

    public void hideRadialMenu() {
        this.saveStateBeforeAnimation();

        final List<Animation> anim = new ArrayList<Animation>();

        final FadeTransition fadeItemGroup = new FadeTransition(Duration.millis(300), this.itemGroup);
        fadeItemGroup.setFromValue(1);
        fadeItemGroup.setToValue(0);
        fadeItemGroup.setOnFinished(new EventHandler<ActionEvent>() {

            @Override
            public void handle(final ActionEvent arg0) {
                RadialMenu.this.itemGroup.setVisible(false);
            }
        });

        anim.add(fadeItemGroup);

        if (this.centerVisibility.get() == CenterVisibility.WITH_MENU) {
            final FadeTransition fadeCenter = new FadeTransition(Duration.millis(300), this.centerGroup);
            fadeCenter.setFromValue(1);
            fadeCenter.setToValue(0);
            fadeCenter.setOnFinished(new EventHandler<ActionEvent>() {

                @Override
                public void handle(final ActionEvent arg0) {
                    RadialMenu.this.centerGroup.setVisible(false);
                }
            });
            anim.add(fadeCenter);
        }

        final ParallelTransition transition = new ParallelTransition(anim.toArray(new Animation[]{}));

        transition.play();
    }

    public void showRadialMenu() {
        final List<Animation> anim = new ArrayList<Animation>();

        final FadeTransition fade = new FadeTransition(Duration.millis(400), this.itemGroup);
        fade.setFromValue(0);
        fade.setToValue(1.0);
        anim.add(fade);

        final Animation offset = new Timeline(new KeyFrame(Duration.ZERO,
                new KeyValue(this.offsetProperty(), 0)), new KeyFrame(
                Duration.millis(300), new KeyValue(this.offsetProperty(),
                this.lastOffsetValue)));
        anim.add(offset);

        final Animation angle = new Timeline(new KeyFrame(Duration.ZERO,
                new KeyValue(this.initialAngleProperty(),
                        this.lastInitialAngleValue + 20)), new KeyFrame(
                Duration.millis(300), new KeyValue(this.initialAngleProperty(),
                this.lastInitialAngleValue)));
        anim.add(angle);

        if (this.centerVisibility.get() == CenterVisibility.WITH_MENU) {
            final FadeTransition fadeCenter = new FadeTransition(Duration.millis(300), this.centerGroup);
            fadeCenter.setFromValue(0);
            fadeCenter.setToValue(1);
            anim.add(fadeCenter);

            // final Animation radius = new Timeline(new KeyFrame(Duration.ZERO,
            // new KeyValue(this.innerRadiusProperty(), 0)), new KeyFrame(
            // Duration.millis(300), new KeyValue(
            // this.innerRadiusProperty(),
            // this.lastInnerRadiusValue)));
            // anim.add(radius);

            RadialMenu.this.centerGroup.setVisible(true);
        }

        final ParallelTransition transition = new ParallelTransition(anim.toArray(new Animation[]{}));

        this.itemGroup.setVisible(true);
        transition.play();
    }

    private void saveStateBeforeAnimation() {
        this.lastInitialAngleValue = this.initialAngle.get();
        this.lastOffsetValue = this.offset.get();
    }

    @Override
    public void changed(final ObservableValue<? extends Object> arg0,
                        final Object arg1, final Object arg2) {
        if(isAllowRedraw())
            redraw();
    }
    public void requestDraw() {
        redraw();
    }

    private void redraw() {
        if (this.centerVisibility.get() == CenterVisibility.NEVER) {
            this.centerGroup.visibleProperty().set(false);
        } else if (this.centerVisibility.get() == CenterVisibility.ALWAYS) {
            this.centerGroup.visibleProperty().set(true);
        } else {
            this.centerGroup.visibleProperty().set(this.itemGroup.isVisible());
        }

        this.centerStrokeShape
                .setFill(this.backgroundVisible.get() ? (this.mouseOn
                        && this.backgroundMouseOnFill.get() != null ? this.backgroundMouseOnFill
                        .get() : this.backgroundFill.get())
                        : Color.TRANSPARENT);
        this.centerStrokeShape
                .setStroke(this.strokeVisible.get() ? (this.mouseOn
                        && this.strokeMouseOnFill.get() != null ? this.strokeMouseOnFill
                        .get() : this.strokeFill.get())
                        : Color.TRANSPARENT);
        this.centerStrokeShape.setStrokeWidth(this.strokeWidth.get());
    }

    /**
     * @return the allowRedraw
     */
    public boolean isAllowRedraw() {
        return allowRedraw;
    }

    /**
     * @param allowRedraw the allowRedraw to set
     */
    public void setAllowRedraw(boolean allowRedraw) {
        this.allowRedraw = allowRedraw;
    }
}
