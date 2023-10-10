// Приостановка и возобновление исполнения
// потока современным способом
class NewThread implements Runnable {
    String name; // имя потока исполнения
    Thread t;
    boolean suspendFlag;

    NewThread(String threadName) {
        name = threadName;
        t = new Thread(this, name);
        System.out.println("Новый поток: " + t);
        suspendFlag = false;
        t.start(); // запустить поток исполнения
    }

    // Точка входа в поток исполнения
    public void run() {
        try {
            for (int i = 15; i > 0; i--) {
                synchronized (this) {
                    while (suspendFlag)
                        wait();
                }
                System.out.println(name + ": " + i);
                Thread.sleep(200);
            }
        } catch (InterruptedException e) {
            System.out.println(name + " прерван.");
        }

        System.out.println(name + " завершен.");
    }

    synchronized void mySuspend() {
        suspendFlag = true;
    }

    synchronized void myResume() {
        suspendFlag = false;
        notify();
    }
}

class SuspendResume {
    public static void main(String args[]) {
        NewThread ob1 = new NewThread("Один");
        NewThread ob2 = new NewThread("Два");

        try {
            Thread.sleep(1000);
            ob1.mySuspend();
            System.out.println("Приостановка потока Один");
            Thread.sleep(1000);
            ob1.myResume();
            System.out.println("Возобновление потока Один");

            ob2.mySuspend();
            System.out.println("Приостановка потока Два");
            Thread.sleep(1000);
            ob2.myResume();
            System.out.println("Возобновление потока Два");
        } catch (InterruptedException e) {
            System.out.println("Главный поток прерван");
        }

        // Ожидание завершения потоков исполнения
        try {
            System.out.println("Ожидание завершения потоков.");
            ob1.t.join();
            ob2.t.join();
        } catch (InterruptedException e) {
            System.out.println("Главный поток прерван");
        }

        System.out.println("Главный поток завершен");
    }
}
