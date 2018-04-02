package com.Potchen;


/**
 * Created by joepo on 3/29/2018.
 */
public class Statement {

    public static Statement ifStmt(Block block) {
        return new IfStmt(block);
    }

    public static Statement whileDo(Block block) {
        return new WhileDo(block);
    }

    public static Statement assignment(Block block) {
        return new Assignment(block);
    }

    public static Statement print(Block block) {
        return new Print(block);
    }

    public static Statement repeat(Block block) {
        return new Repeat(block);
    }


    public static class Repeat extends Statement {
        public final Block block;

        public Repeat(Block block) {
            this.block = block;
        }
    }

    public static class Print extends Statement {
        public final Block block;

        public Print(Block block) {
            this.block = block;
        }
    }

    public static class Assignment extends Statement {
        public final Block block;

        public Assignment(Block block) {
            this.block = block;
        }
    }

    public static class IfStmt extends Statement {
        public final Block block;

        public IfStmt(Block block) {
            this.block = block;
        }
    }

    public static class WhileDo extends Statement {
        public final Block block;

        public WhileDo(Block block) {
            this.block = block;
        }
    }

}
