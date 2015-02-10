class AddRefrenceIdColumnToQuizzes < ActiveRecord::Migration
  def change
  	add_column :quizzes, :refrence_id, :integer
  end
end
