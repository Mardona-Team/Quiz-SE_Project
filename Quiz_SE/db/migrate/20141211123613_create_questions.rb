class CreateQuestions < ActiveRecord::Migration
  def change
    create_table :questions do |t|
      t.belongs_to :quiz, index: true	
      t.string :title 
      t.integer :right_answer_id

      t.timestamps
    end
  end
end
