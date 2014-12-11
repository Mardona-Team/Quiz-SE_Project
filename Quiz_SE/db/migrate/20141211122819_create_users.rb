class CreateUsers < ActiveRecord::Migration
  def change
    create_table :users do |t|
      t.string :username
      t.string :first_name
      t.string :last_name
      t.string :password
      t.string :email
      t.string :type
      t.string :faculty
      t.string :university
      t.string :department
      t.string :year

      t.timestamps
    end
  end
end
