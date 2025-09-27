package cleancode.minesweeper.asis;

import java.util.Random;
import java.util.Scanner;

public class MinesweeperGame {

    public static final int BOARD_ROW_SIZE = 8;
    public static final int BOARD_COLUMN_SIZE = 10;
    private static final String[][] BOARD = new String[BOARD_ROW_SIZE][BOARD_COLUMN_SIZE];
    private static final Integer[][] NEARBY_LAND_MINE_COUNTS = new Integer[BOARD_ROW_SIZE][BOARD_COLUMN_SIZE];
    private static final boolean[][] LAND_MINES = new boolean[BOARD_ROW_SIZE][BOARD_COLUMN_SIZE];
    public static final int LAND_MINE_COUNT = 10;
    private static int gameStatus = 0; // 0: 게임 중, 1: 승리, -1: 패배

    public static void main(String[] args) {
        showGameStartComments();
        Scanner scanner = new Scanner(System.in);
        initializeGame();
        while (true) {
            printBoard();
            if (hasUserWon()) {
                System.out.println("지뢰를 모두 찾았습니다. GAME CLEAR!");
                break;
            }
            if (hasUserLost()) {
                System.out.println("지뢰를 밟았습니다. GAME OVER!");
                break;
            }

            String cellInput = getCellInputFromUser(scanner);
            String userActionInput = getUserActionInputFromUser(scanner);

            int selectedColumnIndex = getSelectedColumnIndex(cellInput);
            int selectedRowIndex = getSelectedRowIndex(cellInput);

            if (isFlagAction(userActionInput)) {
                drawFlag(selectedRowIndex, selectedColumnIndex);
                checkIfGameOver();
            } else if (isOpenAction(userActionInput)) {
                if (isLandMineCell(selectedRowIndex, selectedColumnIndex)) {
                    drawLandMine(selectedRowIndex, selectedColumnIndex);
                    changeGameStatusToLose();
                    continue;
                } else {
                    open(selectedRowIndex, selectedColumnIndex);
                }
                checkIfGameOver();
            } else {
                System.out.println("잘못된 번호를 선택하셨습니다.");
            }
        }
    }

    private static boolean isLandMineCell(int selectedRowIndex, int selectedColumnIndex) {
        return LAND_MINES[selectedRowIndex][selectedColumnIndex];
    }

    private static void changeGameStatusToLose() {
        gameStatus = -1;
    }

    private static void drawLandMine(int selectedRowIndex, int selectedColumnIndex) {
        BOARD[selectedRowIndex][selectedColumnIndex] = "☼";
    }

    private static boolean isOpenAction(String userActionInput) {
        return userActionInput.equals("1");
    }

    private static void drawFlag(int selectedRowIndex, int selectedColumnIndex) {
        BOARD[selectedRowIndex][selectedColumnIndex] = "⚑";
    }

    private static boolean isFlagAction(String userActionInput) {
        return userActionInput.equals("2");
    }

    private static int getSelectedRowIndex(String cellInput) {
        char cellInputRow = cellInput.charAt(1);
        return convertRowFrom(cellInputRow);
    }

    private static int getSelectedColumnIndex(String cellInput) {
        char cellInputCol = cellInput.charAt(0);
        return convertColumnFrom(cellInputCol);
    }

    private static String getUserActionInputFromUser(Scanner scanner) {
        System.out.println("선택한 셀에 대한 행위를 선택하세요. (1: 오픈, 2: 깃발 꽂기)");
        return scanner.nextLine();
    }

    private static String getCellInputFromUser(Scanner scanner) {
        System.out.println("선택할 좌표를 입력하세요. (예: a1)");
        return scanner.nextLine();
    }

    private static boolean hasUserLost() {
        return gameStatus == -1;
    }

    private static boolean hasUserWon() {
        return gameStatus == 1;
    }

    private static void checkIfGameOver() {
        boolean allCellsOpened = isAllCellsOpened();
        if (allCellsOpened) {
            changeGameStatusToWin();
        }
    }

    private static void changeGameStatusToWin() {
        gameStatus = 1;
    }

    private static boolean isAllCellsOpened() {
        boolean allCellsOpened = true;
        for (int row = 0; row < MinesweeperGame.BOARD_ROW_SIZE; row++) {
            for (int col = 0; col < BOARD_COLUMN_SIZE; col++) {
                if (BOARD[row][col].equals("□")) {
                    allCellsOpened = false;
                }
            }
        }
        return allCellsOpened;
    }

    private static int convertColumnFrom(char cellInputCol) {
        switch (cellInputCol) {
            case 'a':
                return 0;
            case 'b':
                 return 1;
            case 'c':
                return 2;
            case 'd':
                return 3;
            case 'e':
                return 4;
            case 'f':
                return 5;
            case 'g':
                return 6;
            case 'h':
                return 7;
            case 'i':
                return 8;
            case 'j':
                return 9;
            default:
                return -1;
        }
    }

    private static int convertRowFrom(char cellInputRow) {
       return  Character.getNumericValue(cellInputRow) - 1;
    }

    private static void printBoard() {
        System.out.println("   a b c d e f g h i j");
        for (int row = 0; row < MinesweeperGame.BOARD_ROW_SIZE; row++) {
            System.out.printf("%d  ", row + 1);
            for (int col = 0; col < BOARD_COLUMN_SIZE; col++) {
                System.out.print(BOARD[row][col] + " ");
            }
            System.out.println();
        }
    }

    private static void initializeGame() {
        for (int row = 0; row < BOARD_ROW_SIZE; row++) {
            for (int col = 0; col < BOARD_COLUMN_SIZE; col++) {
                BOARD[row][col] = "□";
            }
        }
        for (int i = 0; i < LAND_MINE_COUNT; i++) { //지뢰세팅
            int col = new Random().nextInt(BOARD_COLUMN_SIZE);
            int row = new Random().nextInt(BOARD_ROW_SIZE);
            LAND_MINES[row][col] = true;
        }
        for (int row = 0; row < BOARD_ROW_SIZE; row++) {
            for (int col = 0; col < BOARD_COLUMN_SIZE; col++) {
                int count = 0;
                if (!isLandMineCell(row, col)) { //지뢰가 아니라면
                    if (row - 1 >= 0 && col - 1 >= 0 && isLandMineCell(row - 1, col - 1)) {
                        count++;
                    }
                    if (row - 1 >= 0 && isLandMineCell(row - 1, col)) {
                        count++;
                    }
                    if (row - 1 >= 0 && col + 1 < BOARD_COLUMN_SIZE && isLandMineCell(row - 1, col + 1)) {
                        count++;
                    }
                    if (col - 1 >= 0 && isLandMineCell(row, col - 1)) {
                        count++;
                    }
                    if (col + 1 < BOARD_COLUMN_SIZE && isLandMineCell(row, col + 1)) {
                        count++;
                    }
                    if (row + 1 < BOARD_ROW_SIZE && col - 1 >= 0 && isLandMineCell(row + 1, col - 1)) {
                        count++;
                    }
                    if (row + 1 < BOARD_ROW_SIZE && isLandMineCell(row + 1, col)) {
                        count++;
                    }
                    if (row + 1 < BOARD_ROW_SIZE && col + 1 < BOARD_COLUMN_SIZE && isLandMineCell(row + 1, col + 1)) {
                        count++;
                    }
                    NEARBY_LAND_MINE_COUNTS[row][col] = count;
                    continue;
                }
                NEARBY_LAND_MINE_COUNTS[row][col] = 0;
            }
        }
    }

    private static void showGameStartComments() {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        System.out.println("지뢰찾기 게임 시작!");
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
    }

    private static void open(int row, int col) {
        if (row < 0 || row >= BOARD_ROW_SIZE || col < 0 || col >= BOARD_COLUMN_SIZE) {
            return;
        }
        if (!BOARD[row][col].equals("□")) {
            return;
        }
        if (isLandMineCell(row, col)) {
            return;
        }
        if (NEARBY_LAND_MINE_COUNTS[row][col] != 0) {
            BOARD[row][col] = String.valueOf(NEARBY_LAND_MINE_COUNTS[row][col]);
            return;
        } else {
            BOARD[row][col] = "■";
        }
        open(row - 1, col - 1);
        open(row - 1, col);
        open(row - 1, col + 1);
        open(row, col - 1);
        open(row, col + 1);
        open(row + 1, col - 1);
        open(row + 1, col);
        open(row + 1, col + 1);
    }

}
