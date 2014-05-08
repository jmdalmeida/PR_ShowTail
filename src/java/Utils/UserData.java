package Utils;

import java.util.Date;

public class UserData {

    private final int id;
    private final String username, email , name, pathImagem, tipoConta;
    private final Date dataNascimento, dataRegisto;

    public UserData(int id, String username, String email, String name, Date dataNascimento, Date dataRegisto, String pathImagem, String tipoConta) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.name = name;
        this.dataNascimento = dataNascimento;
        this.dataRegisto = dataRegisto;
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

    public Date getDataRegisto() {
        return dataRegisto;
    }

    public Date getDataNascimento() {
        return dataNascimento;
    }

    public String getTipoConta() {
        return tipoConta;
    }

    public String getPathImagem() {
        return pathImagem;
    }

}
