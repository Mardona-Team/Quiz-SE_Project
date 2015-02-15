class CreatePublications < ActiveRecord::Migration
  def change
    create_table :publications do |t|
	   	t.integer :quiz_id
    	t.integer :group_id

    	t.timestamps
    end
  end
end
