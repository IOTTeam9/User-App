package org.techtown.user;

import android.content.Context;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

public class AStarAlgorithm {

    public class Node implements Comparable<Node> {
        int row; // 노드의 행 위치
        int col; // 노드의 열 위치
        int fScore; // f 스코어 (g 스코어와 휴리스틱을 합한 값)
        int gScore; // g 스코어 (시작 노드부터 현재 노드까지의 이동 비용)
        Node parent; // 경로 추적을 위한 부모 노드
        private int numCols; // 미로의 열 수
        private int numRows; // 미로의 행 수

        public Node(int row, int col) {
            this.row = row;
            this.col = col;
            this.fScore = Integer.MAX_VALUE;
            this.gScore = Integer.MAX_VALUE;
            this.parent = null;
        }

        @Override
        public int compareTo(Node other) {
            return Integer.compare(this.fScore, other.fScore);
        }
    }

    private int[][] maze; // 미로 정보를 저장하는 2차원 배열
    private int numRows; // 미로의 행 수
    private int numCols; // 미로의 열 수
    private Node[][] nodes; // 각 위치에 대한 노드 정보를 저장하는 2차원 배열
    private int[][] heuristic; // 각 위치에 대한 휴리스틱 값을 저장하는 2차원 배열

    public AStarAlgorithm(int[][] maze) {
        this.maze = maze;
        this.numRows = maze.length;
        this.numCols = maze[0].length;
        this.nodes = new Node[numRows][numCols];
        this.heuristic = new int[numRows][numCols];

        initializeNodesAndHeuristic();
    }

    private void initializeNodesAndHeuristic() {
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                nodes[i][j] = new Node(i, j);
                heuristic[i][j] = Integer.MAX_VALUE;
            }
        }
    }

    public List<Node> findShortestPath(int startRow, int startCol, int endRow, int endCol) {
        Node startNode = nodes[startRow][startCol];
        Node endNode = nodes[endRow][endCol];

        PriorityQueue<Node> openSet = new PriorityQueue<>();
        List<Node> closedSet = new ArrayList<>();

        startNode.gScore = 0;
        startNode.fScore = calculateFScore(startNode, endNode);

        openSet.add(startNode);

        while (!openSet.isEmpty()) {
            Node current = openSet.poll();

            if (current == endNode) {
                return reconstructPath(current);
            }

            closedSet.add(current);

            List<Node> neighbors = getNeighbors(current, endNode);
            for (Node neighbor : neighbors) {
                if (closedSet.contains(neighbor)) {
                    continue;
                }

                int tentativeGScore = current.gScore + 1;

                if (tentativeGScore < neighbor.gScore) {
                    neighbor.parent = current;
                    neighbor.gScore = tentativeGScore;
                    neighbor.fScore = tentativeGScore + calculateFScore(neighbor, endNode);

                    if (!openSet.contains(neighbor)) {
                        openSet.add(neighbor);
                    } else {
                        // openSet에 이미 있는 neighbor의 fScore가 갱신되었으므로 우선순위 큐를 업데이트합니다.
                        openSet.remove(neighbor);
                        openSet.add(neighbor);
                    }
                }
            }
        }

        return null; // 도착 노드에 도달할 수 없는 경우 빈 리스트 반환
    }


    private List<Node> reconstructPath(Node endNode) {
        List<Node> path = new ArrayList<>();
        Node current = endNode;

        while (current != null) {
            path.add(current);
            current = current.parent;
        }

        Collections.reverse(path);
        return path;
    }

    private boolean isValidLocation(int row, int col) {
        return row >= 0 && row < numRows && col >= 0 && col < numCols && maze[row][col] != 0;
    }

    private List<Node> getNeighbors(Node node, Node endNode) {
        List<Node> neighbors = new ArrayList<>();

        int[][] directions = {
                {-1, 0}, {1, 0}, {0, -1}, {0, 1}, // 상하좌우 방향
                {-1, -1}, {-1, 1}, {1, -1}, {1, 1} // 대각선 방향
        };

        for (int[] direction : directions) {
            int row = node.row + direction[0];
            int col = node.col + direction[1];

            if (isValidLocation(row, col) && maze[row][col] == 1) {
                Node neighbor = nodes[row][col];
                if (neighbor != null && !neighbors.contains(neighbor)) {
                    int neighborGScore;
                    if (direction[0] == 0 || direction[1] == 0) {
                        neighborGScore = node.gScore + 10; // 직선 이동 비용
                    } else {
                        neighborGScore = node.gScore + 14; // 대각선 이동 비용
                    }

                    if (neighbor.gScore > neighborGScore) {
                        neighbor.parent = node;
                        neighbor.gScore = neighborGScore;
                        neighbor.fScore = neighborGScore + calculateFScore(neighbor, endNode);
                    }

                    neighbors.add(neighbor);
                }
            }
        }

        return neighbors;
    }



    private int calculateFScore(Node node, Node endNode) {
        return node.gScore + calculateHeuristic(node, endNode);
    }

    private int calculateHeuristic(Node node, Node endNode) {
        return Math.abs(node.row - endNode.row) + Math.abs(node.col - endNode.col);
    }
    public void showPath(Context context, List<Node> path) {
        if (path == null || path.isEmpty()) {
            showToast(context, "No path found.");
            return;
        }

        StringBuilder pathBuilder = new StringBuilder();
        for (Node node : path) {
            pathBuilder.append("(").append(node.row).append(", ").append(node.col).append(")\n");
        }

        showToast(context, pathBuilder.toString());
    }

    private void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public int getNumCols() {
        return numCols;
    }

    public int getNumRows() {
        return numRows;
    }
}





