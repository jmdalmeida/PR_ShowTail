package Utils;

public class UserData {

    private final int id;
    private final String username, email , name, pass, dataNascimento, dataInscricao, pathImagem, tipoConta;

    public UserData(int id, String username, String email, String name, String pass, String dataNascimento, String dataInscricao, String pathImagem, String tipoConta) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.name = name;
        this.pass = pass;
        this.dataNascimento = dataNascimento;
        this.dataInscricao = dataInscricao;
        this.pathImagem = pathImagem;
        this.tipoConta = tipoConta;
    }

    public int getId() {
        return id;
    }
    
    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return pass;
    }

    public String getDataInscricao() {
        return dataInscricao;
    }

    public String getDataNascimento() {
        return dataNascimento;
    }

    public String getTipoConta() {
        return tipoConta;
    }

    public String getPathImagem() {
        return pathImagem;
    }

}
