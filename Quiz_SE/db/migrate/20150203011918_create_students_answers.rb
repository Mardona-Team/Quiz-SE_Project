class CreateStudentsAnswers < ActiveRecord::Migration
  def change
    create_table :students_answers, id: false do |t|
    	t.integer :student_id
      	t.integer :answer_id
    end
    add_index :students_answers, [:student_id, :answer_id]
  end
end
