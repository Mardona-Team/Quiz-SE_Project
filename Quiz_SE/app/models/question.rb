class Question < ActiveRecord::Base

  
   	has_many    :answers, dependent: :destroy
   	has_one     :right_answer, class_name: 'Answer',foreign_key: 'right_answer_id',dependent: :destroy
   	belongs_to  :quiz

   	



end
