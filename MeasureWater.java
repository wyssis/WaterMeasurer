public class MeasureWater {

    public final int MAX_STEPS = 20;

    public int findTarget(int targetLvl, int smallSize, int bigSize) {
        if (targetLvl > (smallSize + bigSize)) {
            //shouldnt happen now that targetLvl is maximum big+small
            System.out.println("Target level greater than can be contained in both bottles");
            return -1;
        }
        if (targetLvl == smallSize || targetLvl == bigSize) {
            return 1; //just fill one and done
        }
        if (targetLvl == (smallSize + bigSize)) {
            return 2; //just fill both and done
        }

        int steps = -1; //if exception is thrown
        try {
            Bottle smallBottle = new Bottle(smallSize);
            Bottle bigBottle = new Bottle(bigSize);
            //first two steps will always be the same, fill and then transfer
            bigBottle.fill();
            transfer(bigBottle, smallBottle);
            int startBig = pour(targetLvl, smallBottle, bigBottle);
            /*System.out.println("Steps when filling big bottle first: " + 
                (startBig <= MAX_STEPS ? startBig : "more than " + MAX_STEPS));*/

            bigBottle.empty(); //reset before trying other way
            smallBottle.fill();
            transfer(smallBottle, bigBottle);
            int startSmall = pour(targetLvl, smallBottle, bigBottle);
            /*System.out.println("Steps when filling small bottle first: " + 
                (startSmall <= MAX_STEPS ? startSmall : "more than " + MAX_STEPS));*/

            steps = startBig < startSmall ? startBig : startSmall;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        //System.out.println("--------------------------------------------");
        return steps;
    }

    private int pour(int targetLvl, Bottle smallBottle, Bottle bigBottle) throws Exception {
        int steps = 2;
        boolean lastTransfer = true; //if last step was to transfer, this step cant be transfer
        //since either the bottle that was transferred to is full or the other one is empty

        while (smallBottle.currLvl != targetLvl && bigBottle.currLvl != targetLvl &&
            (smallBottle.currLvl + bigBottle.currLvl != targetLvl)) {
            //System.out.println("Step: " + steps);
            //System.out.println("Small bottle: " + smallBottle.currLvl);
            //System.out.println("Big bottle: " + bigBottle.currLvl);
            steps++;
            if (steps > MAX_STEPS) { //break if it takes too long
                break;
            }

            if (bigBottle.currLvl != 0 && smallBottle.currLvl != 0) {
                //neither empty, either empty the full one or transfer from it
                if (bigBottle.currLvl == bigBottle.limit) {
                    if (lastTransfer) {
                        bigBottle.empty();
                        lastTransfer = false;
                    } else {
                        transfer(bigBottle, smallBottle);
                        lastTransfer = true;
                    }
                } else if (smallBottle.currLvl == smallBottle.limit) {
                    if (lastTransfer) {
                        smallBottle.empty();
                        lastTransfer = false;
                    } else {
                        transfer(smallBottle, bigBottle);
                        lastTransfer = true;
                    }
                } else {
                    throw new Exception("Unexpected state: both bottles have water in them, but neither is full");
                }
            } else if (bigBottle.currLvl != bigBottle.limit && smallBottle.currLvl != smallBottle.limit) {
                //neither full, either fill the empty one or transfer to it
                if (bigBottle.currLvl == 0) {
                    if (lastTransfer) {
                        bigBottle.fill();
                        lastTransfer = false;
                    } else {
                        transfer(smallBottle, bigBottle);
                        lastTransfer = true;
                    }
                } else if (smallBottle.currLvl == 0) {
                    if (lastTransfer) {
                        smallBottle.fill();
                        lastTransfer = false;
                    } else {
                        transfer(bigBottle, smallBottle);
                        lastTransfer = true;
                    }
                } else {
                    throw new Exception("Unexpected state: both bottles have room left, but neither is empty");
                }
            } else {
                throw new Exception("Unexpected state: one bottle is full, the other is empty");
            }
        }

        //System.out.println("Small: " + smallBottle.currLvl + " | Big: " + bigBottle.currLvl);
        return steps;
    }

    private void transfer(Bottle bottleFrom, Bottle bottleTo) {
        int roomLeft = bottleTo.limit - bottleTo.currLvl;
        int totalLvl = bottleFrom.currLvl + bottleTo.currLvl;

        if (bottleFrom.currLvl <= roomLeft) {
            bottleTo.currLvl = totalLvl;
            bottleFrom.empty();
        } else {
            bottleTo.currLvl = bottleTo.limit;
            bottleFrom.currLvl = bottleFrom.currLvl - roomLeft;
        }
    }

    class Bottle {
        int limit;
        int currLvl;

        private Bottle(int limit) {
            this.limit = limit;
            this.currLvl = 0;
        }

        private void fill() {
            currLvl = limit;
        }

        private void empty() {
            currLvl = 0;
        }
    }
}
