package com.giulia.calculadora;

import static java.lang.String.valueOf;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public TextView display, screenTitle;
    public Button c, parenteses, porcentagem, div, multi, sub, soma, igual, sinal, ponto, b0, b1, b2, b3, b4, b5, b6, b7, b8, b9;
    public ImageButton delete;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        display = findViewById(R.id.display);
        c = findViewById(R.id.button_c);
        screenTitle = findViewById(R.id.screenTitle);
        igual = findViewById(R.id.button_igual);
        sinal = findViewById(R.id.button_sinal);
        ponto = findViewById(R.id.button_ponto);
        delete = findViewById(R.id.delete);
        Button[] operacoes = {
                div = findViewById(R.id.button_div),
                multi = findViewById(R.id.button_multi),
                sub = findViewById(R.id.button_sub),
                soma = findViewById(R.id.button_soma),
        };

        parenteses = findViewById(R.id.button_parenteses);
        porcentagem = findViewById(R.id.button_porcentagem);

        Button[] numeros = {
                b0 = findViewById(R.id.button_0),
                b1 = findViewById(R.id.button_1),
                b2 = findViewById(R.id.button_2),
                b3 = findViewById(R.id.button_3),
                b4 = findViewById(R.id.button_4),
                b5 = findViewById(R.id.button_5),
                b6 = findViewById(R.id.button_6),
                b7 = findViewById(R.id.button_7),
                b8 = findViewById(R.id.button_8),
                b9 = findViewById(R.id.button_9)
        };

        for (int i = 0; i <= 9; i++) {
            numeros[i].setOnClickListener((View.OnClickListener) this);
        }

        parenteses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textoDisplay = display.getText().toString();

                if (textoDisplay.isEmpty()) {
                    display.setText("(");
                } else if (textoDisplay.endsWith(")")) {
                    display.setText(textoDisplay + "×(");
                } else if (!("%(".contains(Character.toString(textoDisplay.charAt(textoDisplay.length() - 1))))) {
                    if (!textoDisplay.contains("(") && !textoDisplay.contains(")")) {
                        display.setText(textoDisplay + "(");
                    } else if (textoDisplay.endsWith(")")) {
                        display.setText(textoDisplay + "(");
                    } else {
                        // Conta o número de parênteses abertos e fechados
                        int nParentesesAbertos = textoDisplay.split("\\(").length - 1;
                        int nParentesesFechados = textoDisplay.split("\\)").length - 1;

                        if (nParentesesAbertos > nParentesesFechados) {
                            display.setText(textoDisplay + ")");
                        } else {
                            display.setText(textoDisplay + "(");
                        }
                    }
                }
            }
        });

        sinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textoDisplay = display.getText().toString();
                if (textoDisplay.isEmpty()) {
                    display.setText("(-");
                } else if (textoDisplay.endsWith("(-")) {
                    // Remove o "(-" se já estiver presente
                    display.setText(textoDisplay.substring(0, textoDisplay.length() - 2));
                } else {
                    display.setText(textoDisplay + "(-");
                }
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textoDisplay = display.getText().toString();
                if (!textoDisplay.isEmpty()) {
                    String texto = textoDisplay.substring(0, textoDisplay.length() - 1);
                    display.setText(texto);
                }
            }
        });

        ponto.setOnClickListener((View.OnClickListener) this);

        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                display.setText("");
            }
        });

        for (int i = 0; i < operacoes.length; i++) {
            operacoes[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Button bClicado = (Button) v;
                    String novoTexto = bClicado.getText().toString();
                    String textoDisplay = display.getText().toString();
                    if (!textoDisplay.isEmpty() && !("+-×÷%(".contains(Character.toString(textoDisplay.charAt(textoDisplay.length() - 1))))) {
                        String texto = textoDisplay + novoTexto;
                        display.setText(texto);
                    }
                }
            });

            porcentagem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Button bClicado = (Button) v;
                    String novoTexto = bClicado.getText().toString();
                    String textoDisplay = display.getText().toString();
                    if (!textoDisplay.isEmpty() && !("+-×÷%(".contains(Character.toString(textoDisplay.charAt(textoDisplay.length() - 1))))) {
                        String texto = textoDisplay + novoTexto;
                        display.setText(texto);
                    }
                }
            });
        }

        igual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String expressao = display.getText().toString();

                if (!expressao.isEmpty() && !("+-×÷%".contains(Character.toString(expressao.charAt(expressao.length() - 1))))) {
                    try {
                        double resultado = Calcular(expressao);
                        display.setText(valueOf(resultado));
                    } catch (IllegalArgumentException e) {
                        display.setText("Erro: " + e.getMessage());
                    }
                }
            }
        });
    }

    public void onClick(View v) {
        Button bClicado = (Button) v;
        String textoDisplay = display.getText().toString();
        String novoTexto = bClicado.getText().toString();

        if (novoTexto.charAt(0) == '.') {
            if (podeColocarPonto(textoDisplay)) {
                String texto = textoDisplay + novoTexto;
                display.setText(texto);
            } else {
                display.setText(textoDisplay);
            }
        } else {
            String texto = textoDisplay + novoTexto;
            display.setText(texto);
        }
    }

    public static boolean podeColocarPonto(String textoDisplay) {

        if (textoDisplay.isEmpty()) {
            return false;
        }

        int posicaoMais = textoDisplay.lastIndexOf('+');
        int posicaoMenos = textoDisplay.lastIndexOf('-');
        int posicaoMult = textoDisplay.lastIndexOf('×');
        int posicaoDiv = textoDisplay.lastIndexOf('÷');
        int posicaoPrc = textoDisplay.lastIndexOf('%');
        int posicaoPrn1 = textoDisplay.lastIndexOf('(');
        int posicaoPrn2 = textoDisplay.lastIndexOf(')');

        int posicaoUltimaOp = Math.max(
                Math.max(Math.max(posicaoMais, posicaoMenos), Math.max(posicaoMult, posicaoDiv)),
                Math.max(posicaoPrc, Math.max(posicaoPrn1, posicaoPrn2))
        );

        String ultimoNumero;

        if (posicaoUltimaOp == -1) {
            ultimoNumero = textoDisplay;
        } else {
            ultimoNumero = textoDisplay.substring(posicaoUltimaOp + 1);
        }

        return (!ultimoNumero.contains(".")) && (!ultimoNumero.isEmpty());
    }

    public static double Calcular(String expressao) {
        StringBuilder numeroAtual = new StringBuilder();
        ArrayList<Double> numeros = new ArrayList<>();
        ArrayList<Character> operadores = new ArrayList<>();

        for (int i = 0; i < expressao.length(); i++) {
            char c = expressao.charAt(i);

            if (c == '-') {
                //Verificando se o sinal negativo está no início ou após um '('
                if (numeroAtual.length() == 0) {
                    if (i > 0 && expressao.charAt(i - 1) == '(') {
                        numeroAtual.append(c);

                    } else if (i == 0) {
                        numeroAtual.append(c);
                    }
                } else {
                    // É um operador de subtração
                    if (numeroAtual.length() > 0) {
                        numeros.add(Double.parseDouble(numeroAtual.toString()));
                        numeroAtual.setLength(0);
                    }
                    operadores.add(c);
                }

            } else if (Character.isDigit(c) || c == '.') {
                // Constrói o número atual (até encontrar um operador)
                numeroAtual.append(c);
            } else if (c == '+' || c == '-' || c == '×' || c == '÷' || c == '%') {
                if (numeroAtual.length() > 0) {
                    numeros.add(Double.parseDouble(numeroAtual.toString()));
                    numeroAtual.setLength(0); // Limpa o número atual
                }
                // Adiciona o operador apenas se não for o último caractere ou se for seguido por um número ou '('
                if (i < expressao.length() - 1 && (Character.isDigit(expressao.charAt(i + 1)) || expressao.charAt(i + 1) == '(' || expressao.charAt(i+1) == '-')) {
                    operadores.add(c);
                } else if (i == expressao.length() - 1) {
                    throw new IllegalArgumentException("Operador no final da expressão: " + c);
                }
            } else if (c == '(') {
                int fimParenteses = encontrarParentesesFechados(expressao, i);
                String subExpressao = expressao.substring(i + 1, fimParenteses);
                if (subExpressao.isEmpty()){
                    throw new IllegalArgumentException("Expressão vazia dentro dos parênteses.");
                }
                double resultadoSubExpressao = Calcular(subExpressao);
                numeros.add(resultadoSubExpressao);
                // Pula para depois do parêntese de fechamento
                i = fimParenteses;

            } else if (c == ')') {
                throw new IllegalArgumentException("Parêntese fechado inesperado");
            } else {
                throw new IllegalArgumentException("Caractere inválido: " + c);
            }
        }

        if (numeroAtual.length() > 0) {
            numeros.add(Double.parseDouble(numeroAtual.toString()));
        }

        if (!operadores.isEmpty() && numeros.size() > 0 && operadores.size() != numeros.size() - 1) {
            throw new IllegalArgumentException("Expressão inválida: número de operadores não corresponde ao número de números.");
        }

        // Prioridade para parenteses, multiplicação, divisão e porcentagem
        for (int i = 0; i < operadores.size(); i++) {
            char op = operadores.get(i);
            if (op == '×' || op == '÷' || op == '%') {
                double n1 = numeros.get(i);
                double n2 = numeros.get(i + 1);
                double resultadoParcial;
                if (op == '×') {
                    resultadoParcial = n1 * n2;
                } else if (op == '÷') {
                    if (n2 == 0) throw new IllegalArgumentException("Divisão por zero");
                    resultadoParcial = n1 / n2;
                } else { // op == '%'
                    resultadoParcial = n1 * (n2 / 100);
                }
                numeros.set(i, resultadoParcial);
                numeros.remove(i + 1);
                operadores.remove(i);
                i--;
            }
        }

        if (numeros.isEmpty()) {
            return 0;
        }

        double resultadoFinal = numeros.get(0);
        for (int i = 0; i < operadores.size(); i++) {
            char op = operadores.get(i);
            double n = numeros.get(i + 1);
            if (op == '+') {
                resultadoFinal += n;
            } else if (op == '-') {
                resultadoFinal -= n;
            } else {
                throw new IllegalArgumentException("Operador inesperado após priorização: " + op);
            }
        }

        return resultadoFinal;
    }

    private static int encontrarParentesesFechados(String expressao, int inicio) {
        int contador = 1;
        for (int i = inicio + 1; i < expressao.length(); i++) {
            char c = expressao.charAt(i);
            if (c == '(') {
                contador++;
            } else if (c == ')') {
                contador--;
                if (contador == 0) {
                    return i; // Retorna o índice do parêntese fechado
                }
            }
        }
        throw new IllegalArgumentException("Parênteses não balanceados.");
    }
}