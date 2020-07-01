package com.huawei.probation.packagedeliveryapp.util;

public enum DirectionType {

    WALKING("walking"),
    BICYCLING("bicycling"),
    DRIVING("driving");

        private final String directionType;

        DirectionType(String directionType) {
            this.directionType = directionType;
        }

        public String getDirectionType() {
            return this.name();
        }

        public String getDirectionString(){
            return this.directionType;
        }

}
