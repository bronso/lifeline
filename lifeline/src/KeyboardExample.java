

import java.util.Iterator;
import java.util.List;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public final class KeyboardExample extends Application {
    @Override
    public void start(final Stage stage) {
        
    	//initialiserer class keyboard. Antar at den defineres senere. 
    	//Legger Keys i keyboard. 
    	final Keyboard keyboard = new Keyboard(new Key(KeyCode.A),
                                               new Key(KeyCode.S),
                                               new Key(KeyCode.D),
                                               new Key(KeyCode.F));

        final Scene scene = new Scene(new Group(keyboard.createNode())); //legger keyboard i gruppe. Legger gruppe i scene
        stage.setScene(scene);
        stage.setTitle("Keyboard Example");
        stage.show();
    }

    public static void main(final String[] args) {
        launch(args);
    }

    private static final class Key { //class key. Ekstenderer i seg selv ikke noen node. Returnerer bare en node ved behov. 
        private final KeyCode keyCode; //keycode er class som holder hvilken key det er
        private final BooleanProperty pressedProperty; //er key trykket ned?

        public Key(final KeyCode keyCode) { //constructor
            this.keyCode = keyCode; //definerer keycode. 
            this.pressedProperty = new SimpleBooleanProperty(this, "pressed"); //hva er dette? Hvorfor starter den som pressed?
        }

        public KeyCode getKeyCode() { //getter
            return keyCode;
        }

        public boolean isPressed() { //getter
            return pressedProperty.get();
        }

        public void setPressed(final boolean value) { //setter
            pressedProperty.set(value);
        }

        public Node createNode() { //lager knapp for key
            final StackPane keyNode = new StackPane(); //lager stackpane
            keyNode.setFocusTraversable(true); //bestemmer om man kan bruke tab
            installEventHandler(keyNode); //legger handle til node

            final Rectangle keyBackground = new Rectangle(50, 50);
            keyBackground.fillProperty().bind( //bestemmer bakgrunnsfarge. OBS, kan legge logikk i binding!
                    Bindings.when(pressedProperty)
                            .then(Color.RED)
                            .otherwise(Bindings.when(keyNode.focusedProperty())
                                               .then(Color.LIGHTGRAY)
                                               .otherwise(Color.WHITE)));
            keyBackground.setStroke(Color.BLACK);
            keyBackground.setStrokeWidth(2);
            keyBackground.setArcWidth(12);
            keyBackground.setArcHeight(12);

            final Text keyLabel = new Text(keyCode.getName());
            keyLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
            
            keyNode.getChildren().addAll(keyBackground, keyLabel);

            return keyNode;
        }

        private void installEventHandler(final Node keyNode) { //installerer event handle
            // handler for enter key press / release events, other keys are
            // handled by the parent (keyboard) node handler
            final EventHandler<KeyEvent> keyEventHandler =
                    new EventHandler<KeyEvent>() {
                        public void handle(final KeyEvent keyEvent) {
                            if (keyEvent.getCode() == KeyCode.ENTER) { //hvis en key er trykket ned, setPressed()?
                                setPressed(keyEvent.getEventType()
                                               == KeyEvent.KEY_PRESSED);

                                keyEvent.consume(); //denne eventen er nå ferdig prossesert
                            }
                            
                        }
                        
                    };
            
           keyNode.setOnKeyPressed(keyEventHandler); //bruker samme handle på to forskjellige eventer. 
           keyNode.setOnKeyReleased(keyEventHandler);
        }
    }

    private static final class Keyboard { //dette er keyboard som holder keys
        private final Key[] keys;

        public Keyboard(final Key... keys) { //kan ta uendelig antall keys. hmmm....
            this.keys = keys.clone(); //hvorfor må man lage en klone?
        }

        public Node createNode() {
            final HBox keyboardNode = new HBox(6); //lager rammeverk
            keyboardNode.setPadding(new Insets(6));

            final List<Node> keyboardNodeChildren = keyboardNode.getChildren();
            for (final Key key: keys) {
                keyboardNodeChildren.add(key.createNode());
            }

            installEventHandler(keyboardNode);
            return keyboardNode;
        }

        private void installEventHandler(final Parent keyboardNode) { //nå begynner magien.
            // handler for key pressed / released events not handled by
            // key nodes
            final EventHandler<KeyEvent> keyEventHandler =
                    new EventHandler<KeyEvent>() {
                        public void handle(final KeyEvent keyEvent) {
                            final Key key = lookupKey(keyEvent.getCode());
                            if (key != null) {
                                key.setPressed(keyEvent.getEventType()
                                                   == KeyEvent.KEY_PRESSED);

                                keyEvent.consume();//hvis key finnes defineres den som pressed?
                            }
                        }
                    };

            keyboardNode.setOnKeyPressed(keyEventHandler);
            keyboardNode.setOnKeyReleased(keyEventHandler);
            
            //her brukes eventHandler. Ser ut som denne trigges av en KeyEvent.KEY_PRESSED event? tror det er dette som er poenget. 
            keyboardNode.addEventHandler(KeyEvent.KEY_PRESSED,
                                         new EventHandler<KeyEvent>() {
                                             public void handle(final KeyEvent keyEvent) {
                                                 handleFocusTraversal( //hva gjør denne?
                                                         keyboardNode,
                                                         keyEvent);
                                             }
                                         });
        }
      
        private Key lookupKey(final KeyCode keyCode) { //returnerere key objekt basert på numer
            for (final Key key: keys) {
                if (key.getKeyCode() == keyCode) {
                    return key;
                }
            }
            return null;
        }

        private static void handleFocusTraversal(final Parent traversalGroup,
                                                 final KeyEvent keyEvent) {
            final Node nextFocusedNode;
            switch (keyEvent.getCode()) {
                case LEFT:
                    nextFocusedNode =
                            getPreviousNode(traversalGroup,
                                            (Node) keyEvent.getTarget());
                    keyEvent.consume();
                    break;

                case RIGHT:
                    nextFocusedNode =
                            getNextNode(traversalGroup,
                                        (Node) keyEvent.getTarget());
                    keyEvent.consume();
                    break;

                default:
                    return;
            }

            if (nextFocusedNode != null) {
                nextFocusedNode.requestFocus();
            }
        }

        private static Node getNextNode(final Parent parent,
                                        final Node node) {
            final Iterator<Node> childIterator =
                    parent.getChildrenUnmodifiable().iterator();

            while (childIterator.hasNext()) {
                if (childIterator.next() == node) {
                    return childIterator.hasNext() ? childIterator.next()
                                                   : null;
                }
            }

            return null;
        }

        private static Node getPreviousNode(final Parent parent,
                                            final Node node) {
            final Iterator<Node> childIterator =
                    parent.getChildrenUnmodifiable().iterator();
            Node lastNode = null;

            while (childIterator.hasNext()) {
                final Node currentNode = childIterator.next();
                if (currentNode == node) {
                    return lastNode;
                }

                lastNode = currentNode;
            }

            return null;
        }
    }
}