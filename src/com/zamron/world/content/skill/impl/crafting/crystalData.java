package com.zamron.world.content.skill.impl.crafting;

    public enum crystalData {

        //TODO

        CRYSTAL_BODY(new int [][]{{8635, 1}, {8634, 5}, {8633, 10}}, 17376, 15664, 35, 1000, 1),
        CRYSTAL_LEGS(new int[][] {{8641, 1}, {8640, 5}, {8639, 10}}, 1741, 1061, 7, 46, 1),
        CRYSTAL_HELM(new int[][] {{8653, 1}, {8652, 5}, {8651, 10}}, 1741, 1167, 9, 52, 1),
        CRYSTAL_WINGS(new int[][] {{8644, 1}, {8643, 5}, {8642, 10}}, 1741, 1063, 11, 67, 1),
        CRYSTAL_GLOVES(new int[][] {{8647, 1}, {8646, 5}, {8645, 10}}, 1741, 1095, 18, 138, 1);

        private int[][] buttonId;
        private int leather, product, level, amount;
        private double xp;

        private crystalData(final int[][] buttonId, final int leather, final int product, final int level, final double xp, final int amount) {
            this.buttonId = buttonId;
            this.leather = leather;
            this.product = product;
            this.level = level;
            this.xp = xp;
            this.amount = amount;
        }

    }
