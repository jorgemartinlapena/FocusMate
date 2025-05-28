import bcrypt
from app.db.connection import get_db_connection

def set_user(username: str, email: str, password: str):
    password_hash = bcrypt.hashpw(password.encode('utf-8'), bcrypt.gensalt()).decode('utf-8')
    
    with get_db_connection() as conn:
        with conn.cursor() as cursor:
            cursor.execute('SET search_path TO "fmSchema";')
            cursor.execute("""
                INSERT INTO users (username, email, password_hash)
                VALUES (%s, %s, %s)
                RETURNING id;
            """, (username, email, password_hash))
            user_id = cursor.fetchone()[0]
            conn.commit()
            
            cursor.execute("""
                SELECT id, username, email, created_at FROM users WHERE id = %s;
            """, (user_id,))
            user = cursor.fetchone()
            if user:
                return {
                    "id": user[0],
                    "username": user[1],
                    "email": user[2],
                    "created_at": user[3]
                }
            return None

def check_user_credentials(username: str, password: str):
    with get_db_connection() as conn:
        with conn.cursor() as cursor:
            cursor.execute('SET search_path TO "fmSchema";')
            cursor.execute("""
                SELECT id, username, email, password_hash, created_at FROM users WHERE username = %s;
            """, (username,))
            user = cursor.fetchone()
            if user and bcrypt.checkpw(password.encode('utf-8'), user[3].encode('utf-8')):
                return {
                    "id": user[0],
                    "username": user[1],
                    "email": user[2],
                    "created_at": user[4]
                }
            return {"error": "Invalid username or password"}
        
def change_user_password(user_id: int, old_password: str, new_password: str):
    new_password_hash = bcrypt.hashpw(new_password.encode('utf-8'), bcrypt.gensalt()).decode('utf-8')
    
    with get_db_connection() as conn:
        with conn.cursor() as cursor:
            cursor.execute('SET search_path TO "fmSchema";')
            cursor.execute("""
                SELECT password_hash FROM users WHERE id = %s;
            """, (user_id,))
            saved_password_hash = cursor.fetchone()
            if saved_password_hash and bcrypt.checkpw(old_password.encode('utf-8'), saved_password_hash[0].encode('utf-8')):
                cursor.execute("""
                    UPDATE users SET password_hash = %s WHERE id = %s;
                """, (new_password_hash, user_id))
                conn.commit()
                return {"message": "Password changed successfully"}
            return {"error": "Old password is incorrect"}