import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.vaccineapp.R
import com.example.vaccineapp.domain.UserDetails

class UsersAdapter(var users: List<UserDetails>) : RecyclerView.Adapter<UsersAdapter.UserViewHolder>() {

    class UserViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val email: TextView = view.findViewById(R.id.email)
        val firstName: TextView = view.findViewById(R.id.firstName)
        val lastName: TextView = view.findViewById(R.id.lastName)
        val userId: TextView = view.findViewById(R.id.userId)
        val role: TextView = view.findViewById(R.id.userRole)
        // Add other views for the remaining user properties
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.user_display_item, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = users[position]
        holder.email.text = user.email
        holder.firstName.text = user.firstName
        holder.lastName.text = user.lastName
        holder.role.text = user.role
        holder.userId.text = user.id.toString()
        // Set other views with the remaining user properties
    }

    override fun getItemCount() = users.size
}