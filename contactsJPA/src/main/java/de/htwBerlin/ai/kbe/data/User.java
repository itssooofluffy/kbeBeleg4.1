package de.htwBerlin.ai.kbe.data;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "user")
@Entity
@Table(name = "User")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column (name=“user_id”)
    private String userId;
    @Column (name=“last_name”)
    private String lastName;
    @Column (name=“first_name”)
    private String firstName;
    private String token;
;
 
    @OneToMany(mappedBy="user", cascade=CascadeType.ALL, orphanRemoval=true, fetch = FetchType.EAGER)
    private Set<Songlist> songlistSet;

    public User() {
    }

    public User(String userId, String lastName, String firstName) {
    	this.setUserId(userId);
    	this.setLastName(lastName);
    	this.firstName = firstName;
    }
       
    public Set<Songlist> getSonglistSet() {
        if(songlistSet == null) {
            songlistSet = new HashSet<>();
        }
        return songlistSet;
    }

    public void setSonglistSet(Set<Songlist> songlistSet) {
        this.songlistSet = songlistSet;
        // Works for JSON, but not for XML
        if(songlistSet != null) {
            this.songlistSet.forEach(a-> a.setUser(this));
        }
    }
    
    public void addSonglist(Songlist songlist) {
        if(songlistSet == null) {
            songlistSet = new HashSet<>();
        }
        songlist.setUser(this);
        this.songlistSet.add(songlist);
    }
    
    @Override
    public String toString() {
        String songlists = "SongLists:\n";
        for(Songlist s:songlistSet) {
            songlists = songlists.concat(s.toString()) + "\n";
        }
        return "User [id=" + id + ", UserId " + userId + ", lastName " + lastName + ", firstName " + firstName + ", songlistSet=" + songlists + "]";
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firtName) {
		this.firstName = firtName;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
