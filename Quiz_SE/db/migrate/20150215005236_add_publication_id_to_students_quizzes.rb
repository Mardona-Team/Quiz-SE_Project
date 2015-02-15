class AddPublicationIdToStudentsQuizzes < ActiveRecord::Migration
  def change
  	add_column :students_quizzes, :publication_id, :integer
  end
end