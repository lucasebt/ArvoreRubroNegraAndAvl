import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;

public class AVLTreeExample {
    static class Node {
        int key;
        int height;
        Node left, right;

        Node(int key) {
            this.key = key;
            this.height = 1;
        }
    }

    private Node root;

    private int height(Node node) {
        if (node == null)
            return 0;
        return node.height;
    }

    private int balanceFactor(Node node) {
        if (node == null)
            return 0;
        return height(node.left) - height(node.right);
    }

    private Node rightRotate(Node y) {
        Node x = y.left;
        Node T2 = x.right;

        x.right = y;
        y.left = T2;

        y.height = Math.max(height(y.left), height(y.right)) + 1;
        x.height = Math.max(height(x.left), height(x.right)) + 1;

        return x;
    }

    private Node leftRotate(Node x) {
        Node y = x.right;
        Node T2 = y.left;

        y.left = x;
        x.right = T2;

        x.height = Math.max(height(x.left), height(x.right)) + 1;
        y.height = Math.max(height(y.left), height(y.right)) + 1;

        return y;
    }

    private Node insert(Node node, int key) {
        if (node == null)
            return new Node(key);

        if (key < node.key)
            node.left = insert(node.left, key);
        else if (key > node.key)
            node.right = insert(node.right, key);
        else
            return node; // Duplicatas não são permitidas

        node.height = 1 + Math.max(height(node.left), height(node.right));

        int balance = balanceFactor(node);

        // Casos de rotação
        if (balance > 1) {
            if (key < node.left.key)
                return rightRotate(node);
            else if (key > node.left.key) {
                node.left = leftRotate(node.left);
                return rightRotate(node);
            }
        } else if (balance < -1) {
            if (key > node.right.key)
                return leftRotate(node);
            else if (key < node.right.key) {
                node.right = rightRotate(node.right);
                return leftRotate(node);
            }
        }

        return node;
    }

    private int countOccurrences(Node node, int key) {
        if (node == null)
            return 0;

        if (key < node.key)
            return countOccurrences(node.left, key);
        else if (key > node.key)
            return countOccurrences(node.right, key);
        else
            return 1 + countOccurrences(node.left, key) + countOccurrences(node.right, key);
    }

    private Node remove(Node node, int key) {
        // Implemente a remoção conforme necessário
        return node;
    }

    private void preencherArvore() {
        long start = System.currentTimeMillis();

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Selecione o arquivo para preencher a árvore AVL");
        int resultado = fileChooser.showOpenDialog(null);

        if (resultado == JFileChooser.APPROVE_OPTION) {
            File arquivo = fileChooser.getSelectedFile();
            try {
                Scanner leitor = new Scanner(arquivo);
                String linha = leitor.nextLine();
                int[] numeros = parseArray(linha);

                for (int num : numeros) {
                    root = insert(root, num);
                }
            } catch (FileNotFoundException e) {
                System.out.println("Arquivo não encontrado. Certifique-se de que o caminho está correto.");
                preencherArvore(); // Chama novamente a função se o arquivo não for encontrado
            }
        } else {
            System.out.println("Nenhum arquivo selecionado. Encerrando o programa.");
            System.exit(0);
        }

        long end = System.currentTimeMillis();
        System.out.println("Tempo necessário para preencher a árvore AVL: " + (end - start) + " milissegundos");
    }

    private int[] parseArray(String linha) {
        String[] elementos = linha.trim().substring(1, linha.length() - 1).split(", ");
        int[] numeros = new int[elementos.length];
        for (int i = 0; i < elementos.length; i++) {
            numeros[i] = Integer.parseInt(elementos[i]);
        }
        return numeros;
    }

    private void operacoesAleatorias() {
        Random random = new Random();
        for (int i = 0; i < 50000; i++) {
            int numeroSorteado = random.nextInt(19999) - 9999;

            if (numeroSorteado % 3 == 0) {
                root = insert(root, numeroSorteado);
            } else if (numeroSorteado % 5 == 0) {
                root = remove(root, numeroSorteado);
            } else {
                int count = countOccurrences(root, numeroSorteado);
                System.out.println("O número " + numeroSorteado + " aparece " + count + " vezes na árvore AVL.");
            }
        }
    }

    public static void main(String[] args) {
        AVLTreeExample tree = new AVLTreeExample();
        tree.preencherArvore();
        tree.operacoesAleatorias();
    }
}