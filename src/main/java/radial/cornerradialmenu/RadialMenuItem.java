/**
 * RadialMenuItem.java
 *
 * Copyright (c) 2011-2015, JFXtras
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the organization nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 * 
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
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.ArcTo;
import javafx.scene.shape.FillRule;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;

public class RadialMenuItem extends Group implements ChangeListener<Object> {

    protected DoubleProperty startAngle = new SimpleDoubleProperty();
    protected DoubleProperty menuSize = new SimpleDoubleProperty(45);
    protected DoubleProperty innerRadius = new SimpleDoubleProperty();
    protected DoubleProperty radius = new SimpleDoubleProperty();
    protected DoubleProperty offset = new SimpleDoubleProperty();
    protected ObjectProperty<Paint> backgroundMouseOnColor = new SimpleObjectProperty<Paint>();
    protected ObjectProperty<Paint> backgroundColor = new SimpleObjectProperty<Paint>();
    protected BooleanProperty backgroundVisible = new SimpleBooleanProperty(true);
    protected BooleanProperty strokeVisible = new SimpleBooleanProperty(true);
    protected ObjectProperty<Paint> strokeColor = new SimpleObjectProperty<Paint>();
    protected ObjectProperty<Paint> strokeMouseOnColor = new SimpleObjectProperty<Paint>();
    protected BooleanProperty clockwise = new SimpleBooleanProperty();
    //ADDING STROKE WIDTH and Other cool stuff support
    protected DoubleProperty strokeWidth = new SimpleDoubleProperty();
    protected ObjectProperty<Effect> effect = new SimpleObjectProperty<Effect>();
    //Outline Support (TRON effects)
    protected BooleanProperty outlineStrokeVisible = new SimpleBooleanProperty(true);
    protected ObjectProperty<Paint> outlineStrokeColor = new SimpleObjectProperty<Paint>();
    protected ObjectProperty<Paint> outlineStrokeMouseOnColor = new SimpleObjectProperty<Paint>();
    protected DoubleProperty outlineStrokeWidth = new SimpleDoubleProperty();
    protected ObjectProperty<Effect> outlineEffect = new SimpleObjectProperty<Effect>();
    //Path Rendering objects
    protected MoveTo moveTo;
    protected ArcTo arcToInner;
    protected ArcTo arcTo;
    protected LineTo lineTo;
    protected LineTo lineTo2;
    protected double innerStartX;
    protected double innerStartY;
    protected double innerEndX;
    protected double innerEndY;
    protected boolean innerSweep;
    protected double startX;
    protected double startY;
    protected double endX;
    protected double endY;
    protected boolean sweep;
    protected double graphicX;
    protected double graphicY;
    protected double translateX;
    protected double translateY;
    protected boolean mouseOn = false;
    protected Path path;
    protected Path outlinePath; //For creating sharply contrasting outline effects
    protected Node graphic;
    protected String text;

    public RadialMenuItem() {
        this.menuSize = new SimpleDoubleProperty(45);
        this.menuSize.addListener(this);
        this.innerRadius.addListener(this);
	this.radius.addListener(this);
	this.offset.addListener(this);
	this.backgroundVisible.addListener(this);
	this.strokeVisible.addListener(this);
	this.clockwise.addListener(this);
	this.backgroundColor.addListener(this);
	this.strokeColor.addListener(this);
	this.strokeWidth.addListener(this);
	this.effect.addListener(this);
	this.backgroundMouseOnColor.addListener(this);
	this.strokeMouseOnColor.addListener(this);
	this.startAngle.addListener(this);
        
        //TRON effects
        outlineStrokeVisible.addListener(this);
        outlineStrokeColor.addListener(this);
        outlineStrokeMouseOnColor.addListener(this);
        outlineStrokeWidth.addListener(this);
        outlineEffect.addListener(this);        
        
	this.path = new Path();
	this.moveTo = new MoveTo();
	this.arcToInner = new ArcTo();
	this.arcTo = new ArcTo();
	this.lineTo = new LineTo();
	this.lineTo2 = new LineTo();

	this.path.getElements().add(this.moveTo);
	this.path.getElements().add(this.arcToInner);
	this.path.getElements().add(this.lineTo);
	this.path.getElements().add(this.arcTo);
	this.path.getElements().add(this.lineTo2);

        outlinePath = new Path();
        outlinePath.getElements().add(moveTo);
	outlinePath.getElements().add(arcToInner);
	outlinePath.getElements().add(lineTo);
	outlinePath.getElements().add(arcTo);
	outlinePath.getElements().add(lineTo2);

	this.getChildren().addAll(this.path, outlinePath);

	this.setOnMouseEntered(new EventHandler<MouseEvent>() {
	    @Override
	    public void handle(final MouseEvent arg0) {
		RadialMenuItem.this.mouseOn = true;
		RadialMenuItem.this.redraw();
	    }
	});

	this.setOnMouseExited(new EventHandler<MouseEvent>() {
	    @Override
	    public void handle(final MouseEvent arg0) {
		RadialMenuItem.this.mouseOn = false;
		RadialMenuItem.this.redraw();
	    }
	});
    }

    public RadialMenuItem(final double menuSize, final Node graphic) {
	this();
        this.menuSize.set(menuSize);
	this.graphic = graphic;
	if (this.graphic != null)
	    this.getChildren().add(this.graphic);
	this.redraw();
    }

    public RadialMenuItem(final double menuSize, final Node graphic,
	    final EventHandler<ActionEvent> actionHandler) {
	this(menuSize, graphic);
	this.addEventHandler(MouseEvent.MOUSE_CLICKED,
            new EventHandler<MouseEvent>() {
                @Override
                public void handle(final MouseEvent paramT) {
                    actionHandler.handle(new ActionEvent(
                            paramT.getSource(), paramT.getTarget()));
                }
            });
	this.redraw();
    }

    public RadialMenuItem(final double menuSize, final String text, final Node graphic) {
	this(menuSize, graphic);
	this.text = text;
	this.redraw();
    }

    public RadialMenuItem(final double menuSize, final String text,
	    final Node graphic, final EventHandler<ActionEvent> actionHandler) {
	this(menuSize, graphic, actionHandler);
	this.text = text;
	this.redraw();
    }
    
    public double getMenuSize() {
        return this.menuSize.get();
    }

    public void setMenuSize(final double width) {
        this.menuSize.set(width);
    }

    public DoubleProperty menuSizeProperty() {
        return this.menuSize;
    } 

    DoubleProperty innerRadiusProperty() {
	return this.innerRadius;
    }

    DoubleProperty radiusProperty() {
	return this.radius;
    }

    DoubleProperty offsetProperty() {
	return this.offset;
    }

    ObjectProperty<Paint> backgroundMouseOnColorProperty() {
	return this.backgroundMouseOnColor;
    }

    ObjectProperty<Paint> backgroundColorProperty() {
	return this.backgroundColor;
    }

    public BooleanProperty clockwiseProperty() {
	return this.clockwise;
    }

    ObjectProperty<Paint> strokeMouseOnColorProperty() {
	return this.strokeMouseOnColor;
    }

    ObjectProperty<Paint> strokeColorProperty() {
	return this.strokeColor;
    }
    
    DoubleProperty strokeWidthProperty() {
	return this.strokeWidth;
    }
    
    ObjectProperty<Effect> effect() {
	return this.effect;
    }    

    ObjectProperty<Paint> outlineStrokeMouseOnColorProperty() {
	return outlineStrokeMouseOnColor;
    }

    ObjectProperty<Paint> outlineStrokeColorProperty() {
	return outlineStrokeColor;
    }
    
    DoubleProperty outlineStrokeWidthProperty() {
	return outlineStrokeWidth;
    }
    
    ObjectProperty<Effect> outlineEffectProperty() {
	return outlineEffect;
    }    
    
    public BooleanProperty strokeVisibleProperty() {
	return this.strokeVisible;
    }

    public BooleanProperty backgroundVisibleProperty() {
	return this.backgroundVisible;
    }

    public BooleanProperty outlineStrokeVisibleProperty() {
	return outlineStrokeVisible;
    }

    public Node getGraphic() {
	return this.graphic;
    }

    public void setStartAngle(final double angle) {
	this.startAngle.set(angle);
    }

    public DoubleProperty startAngleProperty() {
	return this.startAngle;
    }

    public void setGraphic(final Node graphic) {
	if (this.graphic != null) {
	    this.getChildren().remove(this.graphic);
	}
	this.graphic = graphic;
	if (this.graphic != null) {
	    this.getChildren().add(graphic);
	}
	this.redraw();
    }

    public void setText(final String text) {
	this.text = text;
	this.redraw();
    }

    public String getText() {
	return this.text;
    }

    protected void redraw() {
	this.path.setFill(this.backgroundVisible.get() ? (this.mouseOn
            && this.backgroundMouseOnColor.get() != null ? this.backgroundMouseOnColor
            .get() : this.backgroundColor.get())
            : Color.TRANSPARENT);
	this.path.setStroke(this.strokeVisible.get() ? (this.mouseOn
            && this.strokeMouseOnColor.get() != null ? this.strokeMouseOnColor
            .get() : this.strokeColor.get())
            : Color.TRANSPARENT);
        
        this.path.setStrokeWidth(this.strokeWidth.get());
        this.path.setEffect(this.effect.get());
	this.path.setFillRule(FillRule.EVEN_ODD);

        outlinePath.setFill(Color.TRANSPARENT);
	outlinePath.setStroke(outlineStrokeVisible.get() ? (mouseOn
            && outlineStrokeMouseOnColor.get() != null ? outlineStrokeMouseOnColor.get() 
            : outlineStrokeColor.get()) : Color.TRANSPARENT);
        outlinePath.setStrokeWidth(outlineStrokeWidth.get());
        outlinePath.setEffect(outlineEffect.get());
        
        this.computeCoordinates();
	this.update();
    }

    protected void update() {
	final double innerRadiusValue = this.innerRadius.get();
	final double radiusValue = this.radius.get();

	this.moveTo.setX(this.innerStartX + this.translateX);
	this.moveTo.setY(this.innerStartY + this.translateY);

	this.arcToInner.setX(this.innerEndX + this.translateX);
	this.arcToInner.setY(this.innerEndY + this.translateY);
	this.arcToInner.setSweepFlag(this.innerSweep);
	this.arcToInner.setRadiusX(innerRadiusValue);
	this.arcToInner.setRadiusY(innerRadiusValue);

	this.lineTo.setX(this.startX + this.translateX);
	this.lineTo.setY(this.startY + this.translateY);

	this.arcTo.setX(this.endX + this.translateX);
	this.arcTo.setY(this.endY + this.translateY);
	this.arcTo.setSweepFlag(this.sweep);

	this.arcTo.setRadiusX(radiusValue);
	this.arcTo.setRadiusY(radiusValue);

	this.lineTo2.setX(this.innerStartX + this.translateX);
	this.lineTo2.setY(this.innerStartY + this.translateY);

	if (this.graphic != null) {
	    this.graphic.setTranslateX(this.graphicX + this.translateX);
	    this.graphic.setTranslateY(this.graphicY + this.translateY);
	}
    }

    protected void computeCoordinates() {
	final double innerRadiusValue = this.innerRadius.get();
	final double startAngleValue = this.startAngle.get();

	final double graphicAngle = startAngleValue + (this.menuSize.get() / 2.0);
	final double radiusValue = this.radius.get();

	final double graphicRadius = innerRadiusValue
		+ (radiusValue - innerRadiusValue) / 2.0;

	final double offsetValue = this.offset.get();

	if (!this.clockwise.get()) {
	    this.innerStartX = innerRadiusValue
		    * Math.cos(Math.toRadians(startAngleValue));
	    this.innerStartY = -innerRadiusValue
		    * Math.sin(Math.toRadians(startAngleValue));
	    this.innerEndX = innerRadiusValue
		    * Math.cos(Math.toRadians(startAngleValue + this.menuSize.get()));
	    this.innerEndY = -innerRadiusValue
		    * Math.sin(Math.toRadians(startAngleValue + this.menuSize.get()));
	    this.innerSweep = false;

	    this.startX = radiusValue * Math.cos(Math.toRadians(startAngleValue + this.menuSize.get()));
	    this.startY = -radiusValue * Math.sin(Math.toRadians(startAngleValue + this.menuSize.get()));
	    this.endX = radiusValue * Math.cos(Math.toRadians(startAngleValue));
	    this.endY = -radiusValue * Math.sin(Math.toRadians(startAngleValue));

	    this.sweep = true;

	    if (this.graphic != null) {
		this.graphicX = graphicRadius
			* Math.cos(Math.toRadians(graphicAngle))
			- this.graphic.getBoundsInParent().getWidth() / 2.0;
		this.graphicY = -graphicRadius
			* Math.sin(Math.toRadians(graphicAngle))
			- this.graphic.getBoundsInParent().getHeight() / 2.0;

	    }
	    this.translateX = offsetValue
		    * Math.cos(Math.toRadians(startAngleValue + (menuSize.get() / 2.0)));
	    this.translateY = -offsetValue
		    * Math.sin(Math.toRadians(startAngleValue + (menuSize.get() / 2.0)));

	} else if (this.clockwise.get()) {
	    this.innerStartX = innerRadiusValue
		    * Math.cos(Math.toRadians(startAngleValue));
	    this.innerStartY = innerRadiusValue
		    * Math.sin(Math.toRadians(startAngleValue));
	    this.innerEndX = innerRadiusValue
		    * Math.cos(Math.toRadians(startAngleValue + this.menuSize.get()));
	    this.innerEndY = innerRadiusValue
		    * Math.sin(Math.toRadians(startAngleValue + this.menuSize.get()));

	    this.innerSweep = true;

	    this.startX = radiusValue
		    * Math.cos(Math.toRadians(startAngleValue + this.menuSize.get()));
	    this.startY = radiusValue
		    * Math.sin(Math.toRadians(startAngleValue + this.menuSize.get()));
	    this.endX = radiusValue * Math.cos(Math.toRadians(startAngleValue));
	    this.endY = radiusValue * Math.sin(Math.toRadians(startAngleValue));

	    this.sweep = false;

	    if (this.graphic != null) {
		this.graphicX = graphicRadius
			* Math.cos(Math.toRadians(graphicAngle))
			- this.graphic.getBoundsInParent().getWidth() / 2.0;
		this.graphicY = graphicRadius
			* Math.sin(Math.toRadians(graphicAngle))
			- this.graphic.getBoundsInParent().getHeight() / 2.0;
	    }

	    this.translateX = offsetValue
		    * Math.cos(Math.toRadians(startAngleValue + (this.menuSize.get() / 2.0)));
	    this.translateY = offsetValue
		    * Math.sin(Math.toRadians(startAngleValue + (this.menuSize.get() / 2.0)));
	}
    }
    
    @Override
    public void changed(final ObservableValue<? extends Object> arg0,
	    final Object arg1, final Object arg2) {
	this.redraw();
    }

    void setSelected(final boolean selected) {

    }

    boolean isSelected() {
	return false;
    }
}
