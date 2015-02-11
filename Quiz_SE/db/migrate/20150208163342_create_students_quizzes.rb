class CreateStudentsQuizzes < ActiveRecord::Migration
  def change
    create_table :students_quizzes do |t|
    	t.integer :student_id
    	t.integer :quiz_id
    	t.integer :marks

    	t.timestamps
    end
  end
end
