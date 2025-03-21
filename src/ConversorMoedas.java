import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

public class ConversorMoedas extends JFrame {
    private JComboBox<String> moedaOrigem, moedaDestino;
    private JTextField valorEntrada;
    private JLabel resultadoLabel;

    public ConversorMoedas() {
        setTitle("Conversor de Moedas");
        setSize(400, 300);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(6, 1));

        valorEntrada = new JTextField();
        valorEntrada.setPreferredSize(new Dimension(300, 40));
        moedaOrigem = new JComboBox<>(new String[]{"EUR", "USD", "BRL", "JPY", "GBP"});
        moedaDestino = new JComboBox<>(new String[]{"EUR", "USD", "BRL", "JPY", "GBP"});
        JButton converterBotao = new JButton("Converter");
        resultadoLabel = new JLabel("Resultado: -", SwingConstants.CENTER);

        converterBotao.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                converterMoeda();
            }
        });

        add(new JLabel("Valor para converter:", SwingConstants.CENTER));
        add(valorEntrada);
        add(moedaOrigem);
        add(moedaDestino);
        add(converterBotao);
        add(resultadoLabel);
        setVisible(true);
    }

    private void converterMoeda() {
        try {
            String origem = (String) moedaOrigem.getSelectedItem();
            String destino = (String) moedaDestino.getSelectedItem();
            double valor = Double.parseDouble(valorEntrada.getText());

            double taxa = obterTaxaDeCambio(origem, destino);
            double resultado = valor * taxa;
            resultadoLabel.setText(String.format("Resultado: %.2f %s", resultado, destino));
        } catch (Exception e) {
            resultadoLabel.setText("Erro na conversão!");
        }
    }

    private double obterTaxaDeCambio(String origem, String destino) {
        try {
            String urlStr = "https://api.exchangerate-api.com/v4/latest/" + origem;
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            JSONObject jsonResponse = new JSONObject(response.toString());
            return jsonResponse.getJSONObject("rates").getDouble(destino);
        } catch (Exception e) {
            return 0.0;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ConversorMoedas());
    }
}
