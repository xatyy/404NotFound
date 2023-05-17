package ro.notfound.co_gui.logging;

public class ConsoleLogger implements ILog {
    @Override
    public void write(long longVar) {
        System.out.println(longVar);
    }

    @Override
    public void write(String stringVar) {
        System.out.println(stringVar);
    }

    @Override
    public void write(Object... values) {
        for(Object obj:values){
            System.out.print(obj.toString() + " ");
        }
    }

    @Override
    public void writeTime(long value, TimeUnit unit) {
        System.out.println(TimeUnit.toTimeUnit(value, unit));
    }

    @Override
    public void writeTime(String string, long value, TimeUnit unit) {
        System.out.println(string + " " + TimeUnit.toTimeUnit(value, unit) + " " + unit);
    }

    @Override
    public void close() {

    }
}
