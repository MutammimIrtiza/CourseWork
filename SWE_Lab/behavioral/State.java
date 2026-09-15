
/*
    State
    Context or State itself transitions, not the client
    States know about each other, unlike strategy
*/

enum Color {
    RED, YELLOW, GREEN
}


abstract class TrafficLightState {
    TrafficLight context;

    public TrafficLightState(TrafficLight context) {
        this.context = context;
    }
    abstract void handleNext();
}


class RedState extends TrafficLightState {

    public RedState(TrafficLight context) {
        super(context);
    }

    @Override
    void handleNext() {
        context.setState(Color.GREEN);
    }
    
}


class GreenState extends TrafficLightState {
    public GreenState(TrafficLight context) {
        super(context);
    }

    @Override
    void handleNext() {
        context.setState(Color.YELLOW);
    }
}


class TrafficLight {
    private TrafficLightState state;
    private static TrafficLightState redState;
    private static TrafficLightState greenState;

    public TrafficLight() {
        redState = new RedState(this);
        greenState = new GreenState(this);
        state = redState;
    }

    public void setState(Color c) {
        if(c == Color.RED) this.state = redState;
    }

    public void next() {state.handleNext();}

}


public class State {
    public static void main(String[] args) {
        TrafficLight light = new TrafficLight();
        light.next();
        light.next();
        light.next();
    }
}
