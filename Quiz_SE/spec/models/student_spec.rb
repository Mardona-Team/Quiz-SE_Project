require 'rails_helper'

RSpec.describe Student, :type => :model do

  before(:each) do
    @user = create :user, type: Student
  end

  context 'methods' do
    it "answer the quiz" do
      answer_list = create_list(:answer, 3)
      question = create :question, answers: answer_list, right_answer: create(:answer)
      quiz = create :quiz, instructor: @user, questions: [question]
      group = create :group, instructor: @user
      group.quizzes << quiz

      expect(User.find(@user.id).answer_quiz([answer_list.first.id], Publication.find_by(group_id: group.id, quiz_id: quiz.id))).to eq(0.0)
    end
  end
end