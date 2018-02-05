package contacts.emb.dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import contacts.emb.dao.IDaoCompte;
import contacts.emb.dao.IDaoRole;
import contacts.emb.dao.jdbc.util.UtilJdbc;
import contacts.emb.data.Compte;


public class DaoCompte implements IDaoCompte {

	
	// Champs

	private DataSource		dataSource;
	private IDaoRole		daoRole;

	
	// Injecteurs
	
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	public void setDaoRole(IDaoRole daoRole) {
		this.daoRole = daoRole;
	}

	
	// Actions
	
	@Override
	public int inserer(Compte compte)  {

		Connection			cn		= null;
		PreparedStatement	stmt	= null;
		ResultSet 			rs 		= null;
		String				sql;

		try {
			cn = dataSource.getConnection();

			// Insère le compte
			sql = "INSERT INTO compte ( Pseudo, MotDePasse, Email ) VALUES ( ?, ?, ? )";
			stmt = cn.prepareStatement( sql, Statement.RETURN_GENERATED_KEYS  );
			stmt.setString(	1, compte.getPseudo() );
			stmt.setString(	2, compte.getMotDePasse() );
			stmt.setString(	3, compte.getEmail() );
			stmt.executeUpdate();

			// Récupère l'identifiant généré par le SGBD
			rs = stmt.getGeneratedKeys();
			rs.next();
			compte.setId( rs.getInt(1) );
	
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			UtilJdbc.close( rs, stmt, cn );
		}

		// Insère les rôles
		daoRole.insererPourCompte( compte );
		
		// Retourne l'identifiant
		return compte.getId();
	}

	
	@Override
	public void modifier(Compte compte)  {

		Connection			cn		= null;
		PreparedStatement	stmt	= null;
		String 				sql;

		try {
			cn = dataSource.getConnection();

			// Modifie le compte
			sql = "UPDATE compte SET Pseudo = ?, MotDePasse = ?, Email = ? WHERE IdCompte =  ?";
			stmt = cn.prepareStatement( sql );
			stmt.setString(	1, compte.getPseudo() );
			stmt.setString(	2, compte.getMotDePasse() );
			stmt.setString(	3, compte.getEmail() );
			stmt.setInt(	4, compte.getId() );
			stmt.executeUpdate();
			
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			UtilJdbc.close( stmt, cn );
		}

		// Modifie les rôles
		daoRole.modifierPourCompte( compte );
	}

	
	@Override
	public void supprimer(int idCompte)  {

		Connection			cn		= null;
		PreparedStatement	stmt	= null;
		String 				sql;

		// Supprime les rôles
		daoRole.supprimerPourCompte( idCompte );

		try {
			cn = dataSource.getConnection();

			// Supprime le compte
			sql = "DELETE FROM compte WHERE IdCompte = ? ";
			stmt = cn.prepareStatement( sql );
			stmt.setInt( 1, idCompte );
			stmt.executeUpdate();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			UtilJdbc.close( stmt, cn );
		}
	}

	
	@Override
	public Compte retrouver(int idCompte)  {

		Connection			cn		= null;
		PreparedStatement	stmt	= null;
		ResultSet 			rs 		= null;
		String				sql;

		try {
			cn = dataSource.getConnection();

			sql = "SELECT * FROM compte WHERE IdCompte = ?";
            stmt = cn.prepareStatement( sql );
            stmt.setInt( 1, idCompte );
            rs = stmt.executeQuery();

            if ( rs.next() ) {
                return construireCompte( rs );
            } else {
            	return null;
            }
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			UtilJdbc.close( rs, stmt, cn );
		}
	}

	
	@Override
	public List<Compte> listerTout()   {

		Connection			cn		= null;
		PreparedStatement	stmt	= null;
		ResultSet 			rs 		= null;
		String				sql;

		try {
			cn = dataSource.getConnection();

			sql = "SELECT * FROM compte ORDER BY Pseudo";
			stmt = cn.prepareStatement( sql );
			rs = stmt.executeQuery();

			List<Compte> comptes = new ArrayList<>();
			while ( rs.next() ) {
				comptes.add( construireCompte(rs) );
			}
			return comptes;

		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			UtilJdbc.close( rs, stmt, cn );
		}
	}


	@Override
	public Compte validerAuthentification(String pseudo, String motDePasse)  {
		
		Connection			cn		= null;
		PreparedStatement	stmt	= null;
		ResultSet 			rs 		= null;
		String				sql;

		try {
			cn = dataSource.getConnection();

			sql = "SELECT * FROM compte WHERE Pseudo = ? AND MotDePasse = ?";
			stmt = cn.prepareStatement( sql );
			stmt.setString( 1, pseudo );
			stmt.setString( 2, motDePasse );
			rs = stmt.executeQuery();

			if ( rs.next() ) {
				return construireCompte( rs );			
			} else {
				return null;
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			UtilJdbc.close( rs, stmt, cn );
		}
	}


	@Override
	public boolean verifierUnicitePseudo( String pseudo, int idCompte )   {

		Connection			cn		= null;
		PreparedStatement	stmt	= null;
		ResultSet 			rs 		= null;
		String				sql;

		try {
			cn = dataSource.getConnection();

			sql = "SELECT COUNT(*) AS nbComptes"
				+ " FROM compte WHERE Pseudo = ? AND IdCompte <> ?";
			stmt = cn.prepareStatement( sql );
			stmt.setString(	1, pseudo );
			stmt.setInt(	2, idCompte );
			rs = stmt.executeQuery();
			
			rs.next();
			return rs.getInt( "nbComptes" ) == 0;
	
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			UtilJdbc.close( rs, stmt, cn );
		}
	}
	
	
	// Méthodes auxiliaires
	
	private Compte construireCompte( ResultSet rs ) throws SQLException {
		Compte compte = new Compte();
		compte.setId( rs.getInt( "IdCompte" ) );
		compte.setPseudo( rs.getString( "Pseudo" ) );
		compte.setMotDePasse( rs.getString( "MotDePasse" ) );
		compte.setEmail( rs.getString( "Email" ) );
		compte.setRoles( daoRole.listerPourCompte( compte ) );
		return compte;
	}
	
}
